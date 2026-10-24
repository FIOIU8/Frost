# 第三方声明（THIRD_PARTY_NOTICES）

> 本文件登记 Frost 直接或传递依赖的第三方组件及其许可证。
> 登记时机：引入依赖的同一次提交内必须更新本表（CI 会校验，见 `.github/workflows/ci.yml`）。
> 许可证白名单见文末；不在白名单内的依赖必须提 issue 评审后才能合入。

## 依赖清单

| 依赖名称 | 版权所有者 | 许可证 | 链接 |
| --- | --- | --- | --- |
| Miuix（`top.yukonga.miuix.kmp:*` 0.9.4，`miuix-ui` / `miuix-core` / `miuix-preference` / `miuix-icons` / `miuix-blur` / `miuix-squircle` / `miuix-nav` / `miuix-shader`） | 待填（P1 核对 POM `licenses` 与源码文件头） | Apache-2.0 | https://github.com/compose-miuix-ui/miuix |
| Backdrop（`io.github.kyant0:backdrop` 2.0.1） | Kyant（待 P1 核对全称） | Apache-2.0 | https://github.com/Kyant0/AndroidLiquidGlass |
| Shapes（`io.github.kyant0:shapes` 1.2.1，Backdrop 传递依赖） | Kyant（待 P1 核对全称） | Apache-2.0 | https://github.com/Kyant0/AndroidLiquidGlass |
| material-color-utilities（`com.materialkolor:material-color-utilities` 5.0.1，Miuix 的 Monet 取色传递依赖） | **Jordon de Hoog** | **MIT**（必须保留版权声明，见文末原文） | https://github.com/jordond/materialkolor |
| Skiko（`org.jetbrains.skiko:skiko` 0.150.2，Compose Desktop 渲染后端） | JetBrains | Apache-2.0 | https://github.com/JetBrains/skiko |
| Noto Sans SC Regular（`catalog/src/commonMain/composeResources/font/`，随包分发的中文字体） | Google / Noto Project | **OFL-1.1** | https://github.com/notofonts/noto-cjk |

> **字体补录（2026-10-24 21:05）**：为了让离屏渲染与真机用同一套字（此前渲染走
> Skiko 的 CJK 回退字体，字宽与系统黑体不同，导致截图与真机观感不一致），
> 随包分发 Noto Sans SC Regular。OFL-1.1 要求随分发物附带许可与字体名，
> 原文已存于 `catalog/licenses/NotoSansSC-OFL.txt`。
>
> **2026-10-24 审计补录**：`tools/license_audit.py` 扫出的 213 个运行时依赖里，只有
> `com.materialkolor:*` 是 **MIT**，其余全是 Apache-2.0。MIT 要求"在分发时保留版权声明"，
> 单靠本表的行还不够——原文已贴在文末，发布物（jar/zip）里必须带上本文件。

> 状态：**P1 已审计**（2026-10-24）。用 `tools/license_audit.py` 从 Gradle 缓存里的 POM 读取许可证
> （不联网、不猜），当前 213 个运行时依赖：Apache-2.0 210 / MIT 3，**0 禁止 / 0 未知**。
> 重新生成：见 `docs/license-report.md` 顶部命令。

## 许可证白名单

| 结论 | 许可证 |
| --- | --- |
| ✅ 允许 | Apache-2.0、MIT、BSD-2-Clause、BSD-3-Clause、ISC、CC0-1.0、Unlicense |
| ⚠️ 需评审 | MPL-2.0、EPL-2.0、LGPL-2.1/3.0（动态链接且不改源码时） |
| ❌ 禁止 | GPL-2.0/3.0、AGPL-3.0、SSPL、BUSL、任何"非商业/仅学习"自定义条款 |

命中 ❌ 的依赖处理流程：先找替代实现；确无替代则**整体放弃该功能**，不得合入。

## 上游 NOTICE 原文存档

> Apache-2.0 §4(d)：若上游作品包含 NOTICE 文件，分发衍生作品时必须附上其归属声明。
> 引入依赖时把上游 NOTICE 原文逐字粘贴在此（不要改写、不要翻译）。

### Miuix

（待 P1 补齐）

### Backdrop / AndroidLiquidGlass

（待 P1 补齐）

## MIT 许可原文存档（material-color-utilities）

> 来源：https://github.com/jordond/materialkolor/blob/master/LICENSE（2026-10-24 取原文）
> MIT 要求：本段必须随分发物一起提供，不得删改。

```text
MIT License

Copyright (c) 2026 Jordon de Hoog

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
