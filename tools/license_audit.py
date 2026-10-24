#!/usr/bin/env python3
"""依赖许可证审计（离线读 Gradle 缓存里的 POM，不联网、不猜）。

用法：
    ./gradlew :catalog:dependencies --configuration desktopRuntimeClasspath -q > /tmp/deps-desktop.txt
    ./gradlew :catalog-android:dependencies --configuration debugRuntimeClasspath -q > /tmp/deps-android.txt
    python3 tools/license_audit.py /tmp/deps-desktop.txt /tmp/deps-android.txt > docs/license-report.md

判定来源优先级：
    1. 构件自身 POM 的 <licenses>
    2. 若为空，沿 <parent> 链向上找（Kotlin/AndroidX 常在 parent 里声明）
    3. 都没有 → 标 UNKNOWN，**不猜**，并在报告里列为需要人工确认

退出码：0 = 无禁止类许可证；1 = 命中禁止类或许可证未知（可用于 CI 门禁）。
"""

import os
import re
import sys
import glob
import xml.etree.ElementTree as ET
from collections import Counter, defaultdict

# FROST_GRADLE_CACHE 可覆盖（测试回源分支用：指向一个不存在的目录即可）
CACHE = os.environ.get(
    "FROST_GRADLE_CACHE", os.path.expanduser("~/.gradle/caches/modules-2/files-2.1")
)
FETCH_DIR = os.path.join(os.path.expanduser("~"), ".cache", "frost-license-poms")

# POM 可能不在 Gradle 缓存里：Gradle 对带 Gradle Module Metadata 的构件可能只取 .module
# 不取 .pom（CI 全新缓存时尤其明显）。此时按仓库顺序回源取 POM，取不到才算 UNKNOWN。
REPOS = (
    "https://repo1.maven.org/maven2",
    "https://dl.google.com/dl/android/maven2",
)

FORBIDDEN = tuple(x.upper() for x in ("GPL", "AGPL", "SSPL", "BUSL", "Commons Clause"))
ALLOWED = tuple(
    x.upper()
    for x in ("Apache", "MIT", "BSD", "ISC", "CC0", "Unlicense", "EPL", "MPL", "Public Domain")
)

NS = {"m": "http://maven.apache.org/POM/4.0.0"}


def find_pom(group, artifact, version):
    pat = os.path.join(CACHE, group, artifact, version, "*", f"{artifact}-{version}.pom")
    hits = glob.glob(pat)
    if hits:
        return hits[0]

    os.makedirs(FETCH_DIR, exist_ok=True)
    local = os.path.join(FETCH_DIR, f"{group}__{artifact}__{version}.pom")
    if os.path.exists(local):
        return local

    import urllib.request

    rel = f"{group.replace('.', '/')}/{artifact}/{version}/{artifact}-{version}.pom"
    for repo in REPOS:
        try:
            with urllib.request.urlopen(f"{repo}/{rel}", timeout=20) as resp:
                data = resp.read()
            with open(local, "wb") as fh:
                fh.write(data)
            return local
        except Exception:
            continue
    return None


def pom_licenses(pom_path, depth=0, seen=None):
    """返回 [(name, url)]；为空则沿 parent 上溯。"""
    if depth > 4 or pom_path is None:
        return []
    seen = seen or set()
    if pom_path in seen:
        return []
    seen.add(pom_path)
    try:
        root = ET.parse(pom_path).getroot()
    except Exception:
        return []

    out = []
    for lic in root.findall("./m:licenses/m:license", NS):
        name = (lic.findtext("m:name", default="", namespaces=NS) or "").strip()
        url = (lic.findtext("m:url", default="", namespaces=NS) or "").strip()
        if name or url:
            out.append((name or url, url))
    if out:
        return out

    parent = root.find("./m:parent", NS)
    if parent is not None:
        pg = (parent.findtext("m:groupId", default="", namespaces=NS) or "").strip()
        pa = (parent.findtext("m:artifactId", default="", namespaces=NS) or "").strip()
        pv = (parent.findtext("m:version", default="", namespaces=NS) or "").strip()
        if pg and pa and pv:
            return pom_licenses(find_pom(pg, pa, pv), depth + 1, seen)
    return []


def main(argv):
    if len(argv) < 2:
        print(__doc__)
        return 2

    coords = set()
    for path in argv[1:]:
        with open(path, encoding="utf-8", errors="replace") as fh:
            for line in fh:
                if "project :" in line or "project(" in line:
                    continue
                # 依赖树里 "g:a:1.0 -> 2.0" 表示最终选中 2.0，必须取右侧版本，
                # 否则会把被覆盖掉的旧版本也算进审计（曾因此虚增到 328 条）。
                for m in re.finditer(
                    r"([\w.\-]+):([\w.\-]+):([\w.\-]+)(?:\s+->\s+([\w.\-]+))?", line
                ):
                    g, a, v, selected = m.groups()
                    if g.startswith("project") or v.startswith("$"):
                        continue
                    coords.add((g, a, selected or v))

    # 并发预取缺失的 POM：串行回源 213 个构件要 5 分钟，8 线程后约 40 秒
    missing = [c for c in coords if not glob.glob(
        os.path.join(CACHE, c[0], c[1], c[2], "*", f"{c[1]}-{c[2]}.pom")
    )]
    if missing:
        from concurrent.futures import ThreadPoolExecutor

        with ThreadPoolExecutor(max_workers=8) as pool:
            list(pool.map(lambda c: find_pom(*c), missing))

    rows = []
    for g, a, v in sorted(coords):
        pom = find_pom(g, a, v)
        lics = pom_licenses(pom) if pom else []
        if lics:
            name = "; ".join(sorted({n for n, _ in lics}))
            url = next((u for _, u in lics if u), "")
        else:
            name, url = "UNKNOWN", ""
        rows.append((g, a, v, name, url, pom is not None))

    verdict = Counter()
    for _, _, _, name, _, _ in rows:
        up = name.upper()
        if name == "UNKNOWN":
            verdict["unknown"] += 1
        elif any(f in up for f in FORBIDDEN):
            verdict["forbidden"] += 1
        elif any(a in up for a in ALLOWED):
            verdict["allowed"] += 1
        else:
            verdict["review"] += 1

    print("# 依赖许可证报告\n")
    print(
        "> 由 `tools/license_audit.py` 生成；许可证一律取自构件 POM（含 parent 继承），"
        "不靠名单猜。POM 优先读 Gradle 缓存，缺失时回源 repo1 / dl.google.com。"
    )
    print(f"> 依赖总数 **{len(rows)}**：允许 {verdict['allowed']} / 需评审 {verdict['review']} / "
          f"禁止 {verdict['forbidden']} / 未知 {verdict['unknown']}\n")

    by_lic = defaultdict(list)
    for g, a, v, name, _, _ in rows:
        by_lic[name].append(f"{g}:{a}:{v}")

    print("## 许可证分布\n")
    print("| 许可证 | 依赖数 |")
    print("| --- | --- |")
    for name, items in sorted(by_lic.items(), key=lambda kv: -len(kv[1])):
        print(f"| {name} | {len(items)} |")
    print()

    if verdict["forbidden"]:
        print("## ❌ 禁止类许可证（必须处理）\n")
        for g, a, v, name, _, _ in rows:
            if any(f in name.upper() for f in FORBIDDEN):
                print(f"- `{g}:{a}:{v}` → {name}")
        print()

    if verdict["unknown"] or verdict["review"]:
        print("## ⚠️ 需人工确认\n")
        for g, a, v, name, _, had_pom in rows:
            up = name.upper()
            if name == "UNKNOWN" or (
                not any(f in up for f in FORBIDDEN) and not any(x in up for x in ALLOWED)
            ):
                why = "POM 未声明许可证" if name == "UNKNOWN" else f"非白名单：{name}"
                print(f"- `{g}:{a}:{v}` → {why}")
        print()

    print("## 明细\n")
    print("| 坐标 | 许可证 | 链接 |")
    print("| --- | --- | --- |")
    for g, a, v, name, url, _ in rows:
        print(f"| `{g}:{a}:{v}` | {name} | {url or '—'} |")

    return 0 if (verdict["forbidden"] == 0 and verdict["unknown"] == 0) else 1


if __name__ == "__main__":
    sys.exit(main(sys.argv))
