# 项目架构

## 技术栈

- Java 21
- Spring Boot 3.3.4
- MyBatis 3.0.3 (mybatis-spring-boot-starter)
- MySQL 8.0 (mysql-connector-j)
- JUnit 5 + Mockito

## 目录结构

```
src/
├── main/java/com/ai/code/
│   ├── Application.java          # @SpringBootApplication 入口
│   ├── entity/User.java          # 数据实体，对应 user 表
│   ├── mapper/UserMapper.java    # MyBatis Mapper 接口
│   ├── service/UserService.java  # 业务逻辑
│   └── controller/
│       └── UserController.java   # REST API
├── main/resources/
│   ├── application.yml           # 数据源 + MyBatis 配置
│   └── mapper/UserMapper.xml     # SQL 映射
└── test/java/com/ai/code/
    ├── ApplicationTest.java
    ├── mapper/UserMapperTest.java
    ├── service/UserServiceTest.java
    └── controller/UserControllerTest.java
```

## 数据库

- 本地 MySQL 8.0, 数据库 `test_db`, 用户 `test_user`
- 表 `user(id BIGINT AUTO_INCREMENT, name VARCHAR(100), email VARCHAR(100), created_at DATETIME)`
- 应用配置: `application.yml`

## REST API

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | /api/users | 列表 |
| GET | /api/users/{id} | 详情 |
| POST | /api/users | 创建 |
| PUT | /api/users/{id} | 更新 |
| DELETE | /api/users/{id} | 删除 |

## MyBatis 配置

- `map-underscore-to-camel-case: true`
- `type-aliases-package: com.ai.code.entity`
- 开发环境 SQL 通过 `StdOutImpl` 输出
