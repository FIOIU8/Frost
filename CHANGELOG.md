# 更新日志

本文件格式遵循 [Keep a Changelog 1.1.0](https://keepachangelog.com/zh-CN/1.1.0/)，
版本号遵循 [语义化版本 2.0.0](https://semver.org/lang/zh-CN/)。

0.x 阶段允许破坏性变更，但每次都必须在此登记，并给出迁移说明。

## [Unreleased]

### Added

- 仓库骨架：`git` 初始化、`.gitignore`、`.gitattributes`（文本统一 LF，Windows 脚本 CRLF）
- 许可证与合规文件：`LICENSE`（Apache-2.0 全文）、`NOTICE`、`THIRD_PARTY_NOTICES.md`、`MODIFICATIONS.md`
- Gradle 构建基座：wrapper 9.6.0（含官方 sha256 校验）、`gradle/libs.versions.toml` 唯一版本真源
- 约定插件 `build-logic`：`frost.quality`（Spotless + ktlint）、`frost.toolchain`（JDK 21 工具链）
- CI：`.github/workflows/ci.yml`（JDK 21 + Gradle 缓存 + `build` + `spotlessCheck`）
- 文档：`README.md`、`CONTRIBUTING.md`（DCO + Conventional Commits）
- 组件展示页（showcase）与 30 张基线截图（`renderBaseline` 离屏出图）
- Frost 视觉层 v0：token（色/字/尺寸/渐变）+ 14 个原语 + 5 张参考图 1:1 复刻样张，像素级校准
- 玻璃真模糊：`FrostGlassCard` 接 Backdrop 2.0.1（Android 31+ / 桌面 Skiko），无捕获源时回退半透明近似；`LocalFrostBackdrop` / `FrostGlassCapture` 接线原语与 `frost-glass-probe` 验证样张
- 导航胶囊交互：图标槽（`FrostNavItem`）+ 长按拖动橡皮筋回弹（`frostRubberBand`，渐近 20dp，含性质单测）；返回键改纯圆玻璃按钮 `FrostBackButton`（40dp）

### Changed

- `.gitignore`：修正 `.vscode` 与 `screenshots` 的取反写法（忽略整个目录会使 `!` 失效，改为 `dir/*` + `!file`）

### Fixed

- 根工程 `clean` 任务与 base 插件冲突（`tasks.register` → `tasks.named`）
- Spotless 误扫 `.gradle/` 缓存文件导致 `spotlessMiscCheck` 失败

## [0.0.0] - 2026-10-24

### Added

- 项目立项：确认技术底座（Miuix 0.9.4 / Backdrop 2.0.1 / Kotlin 2.4.21 / CMP 1.12.2 / AGP 9.4.1）、
  阶段计划（P0–P7）与质量门。
- Apache-2.0 许可证与上游归属声明骨架。

[Unreleased]: https://github.com/FIOIU8/Frost/compare/v0.0.0...HEAD
[0.0.0]: https://github.com/FIOIU8/Frost/releases/tag/v0.0.0
