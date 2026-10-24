# 第三方声明（THIRD_PARTY_NOTICES）

> 本文件登记 Frost 直接或传递依赖的第三方组件及其许可证。
> 登记时机：引入依赖的同一次提交内必须更新本表（CI 会校验，见 `.github/workflows/ci.yml`）。
> 许可证白名单见文末；不在白名单内的依赖必须提 issue 评审后才能合入。

## 依赖清单

| 依赖名称 | 版权所有者 | 许可证 | 链接 |
| --- | --- | --- | --- |
| Miuix（`top.yukonga.miuix.kmp:*` 0.9.4，`miuix-ui` / `miuix-preference` / `miuix-icons` / `miuix-blur` / `miuix-squircle` / `miuix-nav` / `miuix-shader`） | 待填（P1 核对 POM `licenses` 与源码文件头） | Apache-2.0 | https://github.com/miuix-kotlin-multiplatform/miuix |
| Backdrop（`io.github.kyant0:backdrop` 2.0.1） | Kyant（待 P1 核对全称） | Apache-2.0 | https://github.com/Kyant0/AndroidLiquidGlass |
| Shapes（`io.github.kyant0:shapes` 1.2.1，Backdrop 传递依赖） | Kyant（待 P1 核对全称） | Apache-2.0 | https://github.com/Kyant0/AndroidLiquidGlass |

> 状态：**骨架**。P1 引入真实依赖后，用 `./gradlew :build-logic:dependencies` 或
> `gradle dependencies` 导出完整依赖树，逐项补齐版本、版权人、许可证与链接。

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
