# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 构建与测试命令

```bash
# 编译项目
mvn compile

# 运行所有测试
mvn test

# 运行单个测试类
mvn test -Dtest=AppTest

# 运行单个测试方法
mvn test -Dtest=AppTest#shouldAnswerWithTrue

# 清理并构建（跳过测试）
mvn clean install -DskipTests

# 清理并构建（包含测试）
mvn clean verify

# 打包 JAR
mvn package

# 运行主类
mvn exec:java -Dexec.mainClass="com.ai.code.App"
# 或直接编译后运行
java -cp target/classes com.ai.code.App
```

## 项目架构

这是一个基础的 Maven 单模块 Java 项目，结构如下：

- **Java 21** — 使用 `maven.compiler.release=21`
- **JUnit Jupiter 5.11.0** — 单元测试框架
- **Maven 标准生命周期** — clean, compile, test, package, install, verify, deploy

```
src/
├── main/java/com/ai/code/
│   └── App.java          # 入口类，main 方法
└── test/java/com/ai/code/
    └── AppTest.java      # 单元测试
```

项目目前是 Maven 原型生成的骨架结构，`com.ai.code` 包下包含一个简单的 `App` 类和对应的 `AppTest` 测试类。

### 主要依赖

| 依赖 | 作用域 | 用途 |
|------|--------|------|
| junit-jupiter-api | test | JUnit 5 测试 API |
| junit-jupiter-params | test | 参数化测试支持 |

### Maven 插件

项目锁定了常用 Maven 插件版本：`maven-compiler-plugin` 3.13.0、`maven-surefire-plugin` 3.3.0、`maven-jar-plugin` 3.4.2 等。
