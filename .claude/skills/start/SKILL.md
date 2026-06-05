# Start Skill

当启动开发环境时自动加载，提供项目上下文。

## 项目初始化检查

1. 确保 MySQL 服务正在运行 (`sudo service mysql status`)
2. 确保数据库 test_db 和用户 test_user 存在
3. 确保 JDK 21 可用
4. 运行 `mvn compile` 验证编译通过

## 常用工作流

### 添加新功能
1. 创建 Entity 类
2. 创建 Mapper 接口 + XML
3. 创建 Service 类
4. 创建 Controller 类
5. 编写测试 (Mapper/Service/Controller 三层)
6. 运行 `mvn test` 确认全部通过

### 修改现有功能
1. 找到对应的 Mapper XML 和 Java 文件
2. 修改后运行关联测试
3. 最后运行全部测试确保无回归
