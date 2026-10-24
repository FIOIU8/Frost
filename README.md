# Frost（霜面）

[![CI](https://github.com/FIOIU8/Frost/actions/workflows/ci.yml/badge.svg)](https://github.com/FIOIU8/Frost/actions/workflows/ci.yml)

> **Frost is an open-source UI library. It is not affiliated with, endorsed by, or sponsored by Apple Inc. or the Miuix project. iOS is a trademark of Apple Inc.**
>
> **本项目为非官方开源 UI 组件库，与 Apple Inc.、Miuix 项目无任何隶属、背书或赞助关系。**「iOS」「Liquid Glass」等名称与商标归其各自权利人所有，本项目仅在描述技术形态时引用。

---

## 这是什么

Frost 是一个面向 **Compose Multiplatform** 的开源 UI 组件库，目标是把 [Miuix](https://github.com/compose-miuix-ui/miuix) 完整吃下来（先当消费者，再当维护者），在其上长出 Frost 自己的设计语言与**毛玻璃材质层**，最终以可 `implementation` 依赖的形式发布到 Maven Central。

**当前状态：`P0` 骨架阶段（v0.0.0）—— 还没有任何可依赖的构件。**
现在仓库里只有构建脚手架、许可证与合规文件；`frost-core` / `frost-ui` / `frost-glass` 从 P1 起逐步出现。请不要在生产项目里引用本项目。

## 目标（可验证，不是口号）

| # | 目标 | 判定方式 |
|---|---|---|
| S1 | 外部工程"导入即用" | 空工程 + 1 个依赖 + 1 段主题代码即可运行 |
| S2 | 有明确的材质语言 | 实心 / 着色 / 玻璃三种 surface 语义，主题层统一调参 |
| S3 | 性能不塌 | 中端机滚动 60fps，同屏玻璃层 ≤ 2 |
| S4 | 许可证干净 | 依赖审计 0 个 GPL/AGPL/SSPL，NOTICE 完整 |
| S5 | 可维护 | 上游改动能 1 天内合入 |
| S6 | 可贡献 | CONTRIBUTING + DCO + API 变更流程 |

## 技术底座

| 组件 | 版本 | 说明 |
|---|---|---|
| [Miuix](https://github.com/compose-miuix-ui/miuix) | `0.9.4` | 坐标 `top.yukonga.miuix.kmp:*`（7 个子模块；**聚合构件 `miuix` 停在 0.8.8，不可用**） |
| [Backdrop](https://github.com/Kyant0/AndroidLiquidGlass) | `2.0.1` | `io.github.kyant0:backdrop`，只取背景捕获 + 折射/色散/高光层，不引高层组件 |
| Kotlin / CMP / AGP / Gradle | `2.4.21` / `1.12.2` / `9.4.1` / `9.6.0` | 版本必须贴着 Miuix 走，**不要自行升级**（Compose 编译器与 runtime 错版会在运行期 `NoSuchMethodError`） |
| JDK | `21`（toolchain） | Gradle 本身可在 17 上运行，模块编译需要 21 |
| Android | `compileSdk 37` / `targetSdk 37` / `minSdk 24` | |

> 全部版本只有一个真源：`gradle/libs.versions.toml`。构建脚本里出现硬编码版本号即视为 bug。

## 路线图

| 阶段 | 内容 | 状态 |
|---|---|---|
| P0 | 立项与骨架（git / 许可证 / Gradle / CI） | 进行中 |
| P1 | Miuix 全量接入验证 + 组件清单 + 截图基线 | 未开始 |
| P2 | 源码级 fork（`git subtree`）与双轨比对 | 未开始 |
| P3 | 设计系统与主题层（`frost-core`） | 未开始 |
| P4 | 毛玻璃基础设施（`frost-glass`） | 未开始 |
| P5 | 组件级改造（Top 12，`frost-ui`） | 未开始 |
| P6 | 打磨与交付（截图测试 / 无障碍 / Dokka / Web Demo） | 未开始 |
| P7 | 发布 v0.1.0 到 Maven Central | 未开始 |

## 仓库结构

```
Frost/
├── build-logic/          # 约定插件（frost.quality / frost.toolchain）
├── gradle/
│   ├── libs.versions.toml  # 唯一版本真源
│   └── wrapper/
├── frost-core/           # 设计令牌 / 主题 / 形状（P1 起）
├── frost-ui/             # 组件（P2/P5 起）
├── frost-glass/          # 毛玻璃材质与渲染（P4 起）
├── catalog/              # 演示 App（P1 起）
├── LICENSE / NOTICE / THIRD_PARTY_NOTICES.md / MODIFICATIONS.md
└── CONTRIBUTING.md / CHANGELOG.md
```

## 本地开发

```bash
# 环境要求：JDK 21（toolchain）、Android SDK Platform 37 + Build-Tools 37.0.0
./gradlew build          # 编译 + spotlessCheck
./gradlew spotlessApply  # 提交前格式化

# Desktop 演示（catalog）
# 注意：Gradle 自身也要跑在 JDK 21 上，否则 21 编译出的 class 在 17 上启动会
# 报 UnsupportedClassVersionError（本工程的 toolchain 只约束编译，不约束 Gradle JVM）
JAVA_HOME=/path/to/jdk-21 ./gradlew :catalog:run
```

Android 构建还需要在根目录放 `local.properties`（**该文件已被 gitignore，禁止提交**）：

```properties
sdk.dir=C\:\\Users\\<you>\\AppData\\Local\\Android\\Sdk
```

## 许可证

[Apache License 2.0](LICENSE)。第三方组件与上游 NOTICE 原文见 [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md)。
对上游源码的任何修改都必须登记到 [MODIFICATIONS.md](MODIFICATIONS.md)（Apache-2.0 §4(b) 的要求）。

## 贡献

见 [CONTRIBUTING.md](CONTRIBUTING.md)。提交必须带 `Signed-off-by`（DCO），格式遵循 Conventional Commits。
