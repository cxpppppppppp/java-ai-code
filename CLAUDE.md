# CLAUDE.md

此文件是项目团队共享指令，供 Claude Code (claude.ai/code) 使用。

## 构建命令

| 命令 | 说明 |
|------|------|
| `mvn compile` | 编译 |
| `mvn test` | 运行所有测试 |
| `mvn test -Dtest=UserServiceTest` | 运行单个测试类 |
| `mvn test -Dtest=UserServiceTest#testCreate` | 运行单个测试方法 |
| `mvn spring-boot:run` | 启动应用 |
| `java -jar target/java-ai-code-1.0-SNAPSHOT.jar` | 运行打包的 JAR |

## 技术栈

- Java 21, Spring Boot 3.3.4, MyBatis 3.0.3, MySQL 8.0
- 测试: JUnit 5, Mockito, @WebMvcTest, @MybatisTest

## 项目架构

分层架构: controller → service → mapper → MySQL

| 层 | 包 | 说明 |
|---|----|------|
| Controller | `controller/` | REST API (`/api/users`) |
| Service | `service/` | 业务逻辑 |
| Mapper | `mapper/` | MyBatis 接口 + XML 映射 |
| Entity | `entity/` | 数据实体 |

详细的项目架构、编码规范、测试约定请见 `.claude/rules/` 目录。
