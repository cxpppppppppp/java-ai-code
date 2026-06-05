# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 构建与测试命令

```bash
# 编译项目
mvn compile

# 运行所有测试
mvn test

# 运行单个测试类
mvn test -Dtest=UserServiceTest

# 运行单个测试方法
mvn test -Dtest=UserServiceTest#testCreate

# 跳过测试构建
mvn clean install -DskipTests

# 打包（含测试）
mvn clean verify

# 打包可执行 JAR
mvn package

# 启动应用
java -jar target/java-ai-code-1.0-SNAPSHOT.jar

# 或直接 Maven 启动
mvn spring-boot:run
```

## 项目架构

Spring Boot 3.3.4 + MyBatis 3.0.3 + WebMVC 分层项目，Java 21。

```
src/
├── main/java/com/ai/code/
│   ├── Application.java          # Spring Boot 入口
│   ├── entity/User.java          # 数据实体
│   ├── mapper/UserMapper.java    # MyBatis Mapper 接口
│   ├── service/UserService.java  # 业务逻辑层
│   └── controller/
│       └── UserController.java   # REST API 控制器
├── main/resources/
│   ├── application.yml           # 应用配置（数据源、MyBatis）
│   └── mapper/UserMapper.xml     # MyBatis XML 映射
└── test/java/com/ai/code/
    ├── ApplicationTest.java           # 上下文加载测试
    ├── mapper/UserMapperTest.java     # Mapper 集成测试（@MybatisTest）
    ├── service/UserServiceTest.java   # Service 单元测试（Mockito）
    └── controller/
        └── UserControllerTest.java    # Controller 测试（@WebMvcTest）
```

### 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 3.3.4 | 基础框架 |
| MyBatis Spring Boot | 3.0.3 | 持久层框架 |
| MySQL Connector/J | (parent 管理) | MySQL 驱动 |
| JUnit 5 | (parent 管理) | 测试框架 |
| Mockito | (parent 管理) | Mock 测试 |
| Java | 21 | JDK 版本 |

### 数据库

- 本地 MySQL 8.0，数据库名 `test_db`，用户 `test_user`/`test_pass_123`
- 表 `user(id BIGINT AUTO_INCREMENT, name VARCHAR(100), email VARCHAR(100), created_at DATETIME)`

### REST API

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/users | 获取所有用户 |
| GET | /api/users/{id} | 根据 ID 获取用户 |
| POST | /api/users | 创建用户（body: {name, email}） |
| PUT | /api/users/{id} | 更新用户（body: {name, email}） |
| DELETE | /api/users/{id} | 删除用户 |

### 测试分层策略

- **Mapper 测试** (`@MybatisTest`)：连接真实 MySQL，验证 SQL 映射正确性
- **Service 测试** (`@ExtendWith(MockitoExtension.class)`)：Mock Mapper 层，测试业务逻辑
- **Controller 测试** (`@WebMvcTest`)：Mock Service 层，测试 HTTP 接口响应
- **ApplicationTest** (`@SpringBootTest`)：验证 Spring 上下文正常加载

### MyBatis 配置要点

- `map-underscore-to-camel-case: true` — 数据库下划线字段自动映射为驼峰属性
- `type-aliases-package: com.ai.code.entity` — XML 中可直接用 `User` 而非全限定名
- 开发环境 SQL 日志通过 `StdOutImpl` 输出
