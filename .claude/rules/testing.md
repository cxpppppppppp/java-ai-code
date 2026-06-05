# 测试约定

## 分层测试策略

| 层级 | 注解 | 数据库 | Mock |
|------|------|--------|------|
| Mapper | @MybatisTest | 真实 MySQL | 无 |
| Service | @ExtendWith(MockitoExtension.class) | 无 | Mock Mapper |
| Controller | @WebMvcTest | 无 | Mock Service |
| 集成 | @SpringBootTest | 真实 MySQL | 无 |

## Mapper 测试
- 使用 @MybatisTest + @AutoConfigureTestDatabase(Replace.NONE)
- 连接本地真实 MySQL 数据库 test_db
- 测试 CRUD 各操作
- 验证插入后能获取自增 ID
- 验证更新后数据正确变更
- 验证删除后查询返回 null

## Service 测试
- 使用 Mockito 扩展，Mock Mapper 层
- 测试正常流程和异常流程
- 验证 Mock 方法的调用次数
- 测试 update 时用户不存在的异常处理

## Controller 测试
- 使用 @WebMvcTest + MockMvc
- Mock Service 层
- @MockBean 注入 Mock 依赖
- 测试各种 HTTP 状态码 (200/201/204/400/404)
- 测试请求体和响应体的 JSON 序列化

## 命名规范
- 测试类: XxxTest
- 测试方法: testXxx 或 shouldXxx_whenXxx
- 使用 @DisplayName 提供中文描述（可选）
