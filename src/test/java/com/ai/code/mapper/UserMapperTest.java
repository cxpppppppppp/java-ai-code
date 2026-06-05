package com.ai.code.mapper;

import com.ai.code.entity.User;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.boot.test.autoconfigure.MybatisTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MybatisTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void testFindAll() {
        List<User> users = userMapper.findAll();
        assertNotNull(users);
        assertTrue(users.size() >= 2);
    }

    @Test
    void testFindById() {
        User user = userMapper.findById(1L);
        assertNotNull(user);
        assertEquals("张三", user.getName());
        assertEquals("zhangsan@test.com", user.getEmail());
    }

    @Test
    void testFindByEmail() {
        User user = userMapper.findByEmail("lisi@test.com");
        assertNotNull(user);
        assertEquals("李四", user.getName());
    }

    @Test
    void testInsert() {
        User user = new User();
        user.setName("王五");
        user.setEmail("wangwu@test.com");

        int affected = userMapper.insert(user);
        assertEquals(1, affected);
        assertNotNull(user.getId());

        User saved = userMapper.findById(user.getId());
        assertNotNull(saved);
        assertEquals("王五", saved.getName());
    }

    @Test
    void testUpdate() {
        User user = userMapper.findById(1L);
        assertNotNull(user);
        user.setName("张三-updated");
        user.setEmail("zhangsan_updated@test.com");

        int affected = userMapper.update(user);
        assertEquals(1, affected);

        User updated = userMapper.findById(1L);
        assertEquals("张三-updated", updated.getName());
        assertEquals("zhangsan_updated@test.com", updated.getEmail());
    }

    @Test
    void testDeleteById() {
        // 先插入一条再删除
        User user = new User();
        user.setName("临时用户");
        user.setEmail("temp@test.com");
        userMapper.insert(user);

        int affected = userMapper.deleteById(user.getId());
        assertEquals(1, affected);

        User deleted = userMapper.findById(user.getId());
        assertNull(deleted);
    }
}
