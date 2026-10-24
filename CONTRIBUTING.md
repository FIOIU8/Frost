# 贡献指南（CONTRIBUTING）

感谢你愿意参与 Frost。本文件写得比较"硬"，原因是这个项目建立在另一个开源库（Miuix）之上，**合规与可同步性比代码量更重要**。

## 0. 先看这三条红线

1. **不得引入 GPL / AGPL / SSPL / BUSL 代码或素材**（含从 GPL 项目复制的片段、图标、着色器）。白名单见 [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md)。
2. **不得修改 `docs/` 之外的计划文件来"绕过"约定**；技术结论变更请先提 issue 讨论。
3. **不得在提交里塞构建产物、`local.properties`、IDE 配置**。

## 1. 环境

| 项 | 要求 |
| --- | --- |
| JDK | 21（toolchain；Gradle 守护进程可用 17 启动，但编译要求 21） |
| Android | SDK Platform 37 + Build-Tools 37.0.0 |
| Gradle | 只用 `./gradlew`，**不要用本机 gradle**（版本必须与 wrapper 一致） |

提交前至少跑一遍：

```bash
./gradlew build       # 编译 + spotlessCheck
./gradlew spotlessApply  # 自动修复格式（先跑这个，再 build）
```

## 2. DCO（必须）

所有提交必须带 `Signed-off-by`，即：

```bash
git commit -s -m "feat(frost-ui): add FrostGlassCard"
```

这会生成 `Signed-off-by: Your Name <you@example.com>`，表示你同意 [Developer Certificate of Origin 1.1](https://developercertificate.org/)。

- `user.name` / `user.email` 必须是**真实可联系**的身份，匿名/假邮箱的 sign-off 无效。
- 忘记签字时：`git commit --amend -s --no-edit`（仅限未推送的提交）。
- 本项目**不接受** CLA，贡献按 inbound = outbound 原则以 Apache-2.0 授权。

## 3. 提交规范（Conventional Commits）

```
<type>(<scope>): <subject>

<body>

Signed-off-by: Name <email>
```

**type**：`feat` `fix` `docs` `build` `ci` `refactor` `perf` `test` `chore` `revert`

**scope**（可省略）：

| scope | 覆盖范围 |
| --- | --- |
| `core` | `frost-core`（tokens / theme / shapes） |
| `ui` | `frost-ui`（组件） |
| `glass` | `frost-glass`（毛玻璃） |
| `catalog` | 演示 App |
| `build` | Gradle / build-logic / 版本目录 |
| `ci` | `.github/workflows` |
| `legal` | LICENSE / NOTICE / MODIFICATIONS |
| `gitignore` | 仓库规则文件 |

**subject**：祈使句、小写开头、不加句号，中文英文都可以但**同一次 PR 保持一致**。

**破坏性变更**：type 后加 `!`，并在 body 写 `BREAKING CHANGE:` 段落。

## 4. 分支与 PR

- 主干 `main` 永远可构建。
- 短分支命名：`feat/glass-surface`、`fix/glass-double-blur`。
- 一个 PR 只做一件事；涉及视觉变更必须在描述里附**浅色 + 深色各一张截图**。
- 涉及性能的改动附帧时间对比（`benchmark` 模块输出）。
- 合并方式：squash merge，**保留 sign-off**。

### PR 检查清单（对应质量门）

- [ ] `./gradlew build` 通过
- [ ] `spotlessCheck` 通过
- [ ] 新增/修改的公开 API 有 KDoc
- [ ] 新依赖已登记 `THIRD_PARTY_NOTICES.md`，且许可证在白名单内
- [ ] 改过上游文件已在 `MODIFICATIONS.md` 登记，并在文件头加了 `Modified by Frost contributors`
- [ ] 视觉改动附截图（浅/深）
- [ ] 性能相关附帧时间对比
- [ ] 所有 commit 带 `Signed-off-by`

## 5. 代码约定

- 公开组件统一 `Frost` 前缀；材质参数一律走 `GlassStyle`，不要在组件参数里散落 `blurRadius: Dp`。
- 只从 `FrostTheme.*` 读主题，**禁止**在组件内直接读 `MiuixTheme.*`。
- 库模块内**禁止硬编码颜色**（`Color(0x…)`），一律走主题令牌（CI 会加规则）。
- 无状态优先（`value` + `onValueChange`），内部状态只出现在浮层类组件。
- 可能变动的 API 标 `@ExperimentalFrostApi`。

## 6. 改了上游代码怎么办

P2 起 `third_party/miuix` 是 `git subtree` 引入的上游源码。修改它必须三件套齐备：

1. 文件头加 `Modified by Frost contributors on YYYY-MM-DD (see MODIFICATIONS.md)`
2. 在 [MODIFICATIONS.md](MODIFICATIONS.md) 追加一行（改了什么 / 为什么）
3. commit message 末尾加 `Upstream-Path: <上游原始路径>`

漏了任何一条，PR 不合并——这不是形式主义，是为了两周一次的上游同步能自动定位冲突。

## 7. 商标与命名

- 不在组件名、示例、截图中使用 Apple 商标（`iOS`、`iPhone`、`Liquid Glass` 作为产品名）。
- 描述技术形态时可用中性表述：`glass material`、`backdrop blur`、`refraction`。
- 项目宣传语不得暗示与 Apple 或 Miuix 官方存在合作关系。

## 8. 报告问题

提 issue 请附：复现步骤、期望/实际、设备与系统版本、`./gradlew build` 输出片段。
安全问题请勿公开提 issue，先私下联系维护者。
