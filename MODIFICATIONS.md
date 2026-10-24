# 上游文件修改登记（MODIFICATIONS）

> **为什么有这个文件**：Apache-2.0 §4(b) 要求对修改过的文件"带有醒目的修改声明"。
> Frost 在 P2 起以 `git subtree` 引入 Miuix 源码，随后会直接修改其中的文件（决策 D4）。
> 每改一个上游文件，必须同时完成三件事，缺一不可：
>
> 1. 在**该文件头部**加一行注释：`Modified by Frost contributors on YYYY-MM-DD (see MODIFICATIONS.md)`
> 2. 在**本表**追加一行，写清改了什么、为什么改
> 3. commit message 末尾加 `Upstream-Path: <上游原始路径>` trailer，便于上游同步时定位冲突
>
> 未登记的上游文件修改，PR 一律不合并（§13 质量门）。

## 当前状态

| 项 | 值 |
| --- | --- |
| 上游源码是否已引入 | 否（P2 才引入 `third_party/miuix`） |
| 已登记修改数 | 0 |
| 最近核对上游 commit | 未引入 |

## 修改登记表

| # | 上游文件路径 | 上游 commit | 修改内容摘要 | 理由 | 修改人 | 日期 | PR |
| --- | --- | --- | --- | --- | --- | --- | --- |
| — | （尚无条目） | — | — | — | — | — | — |

<!--
追加格式示例（P2 起启用，删除本注释即可）：

| 1 | `miuix-ui/src/commonMain/kotlin/top/yukonga/miuix/kmp/Surface.kt` | `a1b2c3d` | 新增 `glassStyle` 参数，默认 `Solid` 保持上游行为 | 支持 Glass 材质语义（P3） | FIOIU8 | 2026-11-02 | #12 |
-->

## 与上游同步的关系

- 执行 `git subtree pull` 后，先按本表逐条检查冲突文件，冲突解决以"保留 Frost 修改 + 吸收上游改进"为准。
- 若上游把我们的修改合并进主干，把该行状态改为「已上游化」并保留记录，不要再删除历史行。
