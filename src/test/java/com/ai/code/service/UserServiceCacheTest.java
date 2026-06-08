package com.ai.code.service;

import com.ai.code.entity.User;
import com.ai.code.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * 缓存集成测试 — 验证 Spring Cache + Redis 在 UserService 上的行为。
 *
 * 使用 @MockBean 替换 UserMapper，通过 verify 验证方法调用次数
 * 来判断缓存是否命中。
 */
@SpringBootTest
class UserServiceCacheTest {

    @Autowired
    private UserService userService;

    @Autowired
    private CacheManager cacheManager;

    @MockBean
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        // 每次测试前清空缓存，避免测试间相互影响
        cacheManager.getCache("users").clear();
        // 重置 mock 调用计数
        reset(userMapper);
    }

    @Test
    void testFindById_ShouldCacheResult() {
        // 准备
        User mockUser = createUser(1L, "张三", "zhangsan@test.com");
        when(userMapper.findById(1L)).thenReturn(mockUser);

        // 第一次调用 — 应走 Mapper
        User result1 = userService.findById(1L);
        assertNotNull(result1);
        assertEquals("张三", result1.getName());

        // 第二次调用 — 应走缓存，不再调用 Mapper
        User result2 = userService.findById(1L);
        assertNotNull(result2);
        assertEquals("张三", result2.getName());

        // 验证 findById 只被调用了一次（第二次命中缓存）
        verify(userMapper, times(1)).findById(1L);
    }

    @Test
    void testFindById_WithDifferentIds_ShouldNotShareCache() {
        when(userMapper.findById(1L)).thenReturn(createUser(1L, "张三", "zhangsan@test.com"));
        when(userMapper.findById(2L)).thenReturn(createUser(2L, "李四", "lisi@test.com"));

        userService.findById(1L);
        userService.findById(2L);
        userService.findById(1L);  // 命中缓存
        userService.findById(2L);  // 命中缓存

        // 每个 ID 只应调用一次 Mapper
        verify(userMapper, times(1)).findById(1L);
        verify(userMapper, times(1)).findById(2L);
    }

    @Test
    void testFindByEmail_ShouldCacheResult() {
        when(userMapper.findByEmail("zhangsan@test.com")).thenReturn(
                createUser(1L, "张三", "zhangsan@test.com"));

        userService.findByEmail("zhangsan@test.com");
        userService.findByEmail("zhangsan@test.com");

        verify(userMapper, times(1)).findByEmail("zhangsan@test.com");
    }

    @Test
    void testFindAll_ShouldCacheResult() {
        when(userMapper.findAll()).thenReturn(
                List.of(createUser(1L, "张三", "zhangsan@test.com")));

        userService.findAll();
        userService.findAll();

        verify(userMapper, times(1)).findAll();
    }

    @Test
    void testUpdate_ShouldUpdateCache() {
        // 准备: 先加载一个用户到缓存
        User existingUser = createUser(1L, "张三", "zhangsan@test.com");
        when(userMapper.findById(1L)).thenReturn(existingUser);

        userService.findById(1L);  // 写入缓存 (mapper 调用 #1)

        // 执行更新 — @CachePut 应更新缓存
        // update() 内部会再次调用 mapper.findById(1L) 做存在性检查 (mapper 调用 #2)
        userService.update(1L, "张三-new", "new@test.com");

        // 再次查找 — 应命中缓存，不走 Mapper
        // 把 mock 改为返回 null，如果走了 Mapper 会返回 null
        when(userMapper.findById(1L)).thenReturn(null);

        User cached = userService.findById(1L);
        assertNotNull(cached, "缓存中应存在更新后的用户，不走 Mapper");
        assertEquals("张三-new", cached.getName());
        assertEquals("new@test.com", cached.getEmail());

        // mapper.findById(1L) 应只被调用了 2 次（第 1 次初始写入 + 第 2 次 update 内部），第三次命中缓存
        verify(userMapper, times(2)).findById(1L);
    }

    @Test
    void testCreate_ShouldEvictAllCache() {
        // 准备: 先缓存一个用户
        when(userMapper.findById(1L)).thenReturn(
                createUser(1L, "张三", "zhangsan@test.com"));
        userService.findById(1L);  // 写入缓存

        // 修改 mock: create 后 findById 返回不同数据
        when(userMapper.insert(any(User.class))).thenReturn(1);

        // 执行 create — @CacheEvict 应清空所有缓存
        userService.create("新用户", "new@test.com");

        // 再次查找 — 缓存已被清空，应走 Mapper
        when(userMapper.findById(1L)).thenReturn(
                createUser(1L, "张三", "zhangsan@test.com"));
        userService.findById(1L);

        // verify: cache 被清空，所以第二次 findById 又调用了 Mapper
        verify(userMapper, times(2)).findById(1L);
    }

    @Test
    void testDeleteById_ShouldEvictAllCache() {
        // 准备: 先缓存一个用户
        when(userMapper.findById(1L)).thenReturn(
                createUser(1L, "张三", "zhangsan@test.com"));
        when(userMapper.findById(2L)).thenReturn(
                createUser(2L, "李四", "lisi@test.com"));

        userService.findById(1L);  // 写入缓存
        userService.findById(2L);  // 写入缓存

        // 执行 delete — @CacheEvict 应清空所有缓存
        when(userMapper.deleteById(anyLong())).thenReturn(1);
        userService.deleteById(1L);

        // 再次查找 — 缓存已被清空，应走 Mapper
        when(userMapper.findById(1L)).thenReturn(null);
        when(userMapper.findById(2L)).thenReturn(null);

        userService.findById(1L);
        userService.findById(2L);

        // verify: 第二次查找又调用了 Mapper（缓存被 evict）
        verify(userMapper, times(2)).findById(1L);
        verify(userMapper, times(2)).findById(2L);
    }

    private User createUser(Long id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}
