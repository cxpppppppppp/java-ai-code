# 编码规范

## Java 版本
- 使用 Java 21 特性（Record、Pattern Matching、Text Blocks 等）
- Maven compiler release 设置为 21

## 命名规范
- 类名: PascalCase (UserController, UserService)
- 方法名: camelCase (findAll, createUser)
- 常量: UPPER_SNAKE_CASE
- 包名: 全小写，com.ai.code.*

## 分层规范
- Controller: 处理 HTTP 请求/响应，不包含业务逻辑
- Service: 业务逻辑层，事务管理
- Mapper: MyBatis 接口，只做数据访问
- Entity: 纯数据对象，与数据库表映射

## 依赖注入
- 使用构造器注入，不使用 @Autowired 字段注入
- 使用 final 关键字标记不可变依赖

## API 设计
- RESTful 风格，使用复数名词路径（/api/users）
- 标准 HTTP 状态码: 200/201/204/400/404
- @RequestMapping 类级别指定公共前缀

## 数据库
- 表名: 小写+下划线 (user, order_item)
- 列名: 小写+下划线 (created_at, is_active)
- MyBatis 开启 map-underscore-to-camel-case
