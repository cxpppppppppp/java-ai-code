package com.ai.code.service;

import com.ai.code.entity.User;
import com.ai.code.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserMapper userMapper;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userMapper);
    }

    @Test
    void testFindAll() {
        List<User> mockUsers = List.of(createUser(1L, "张三", "zhangsan@test.com"));
        when(userMapper.findAll()).thenReturn(mockUsers);

        List<User> result = userService.findAll();
        assertEquals(1, result.size());
        verify(userMapper).findAll();
    }

    @Test
    void testFindById() {
        User mockUser = createUser(1L, "张三", "zhangsan@test.com");
        when(userMapper.findById(1L)).thenReturn(mockUser);

        User result = userService.findById(1L);
        assertNotNull(result);
        assertEquals("张三", result.getName());
        verify(userMapper).findById(1L);
    }

    @Test
    void testFindByEmail() {
        User mockUser = createUser(1L, "张三", "zhangsan@test.com");
        when(userMapper.findByEmail("zhangsan@test.com")).thenReturn(mockUser);

        User result = userService.findByEmail("zhangsan@test.com");
        assertNotNull(result);
        assertEquals("张三", result.getName());
        verify(userMapper).findByEmail("zhangsan@test.com");
    }

    @Test
    void testCreate() {
        doAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return 1;
        }).when(userMapper).insert(any(User.class));

        User result = userService.create("新用户", "new@test.com");
        assertNotNull(result);
        assertEquals("新用户", result.getName());
        assertEquals("new@test.com", result.getEmail());
        assertNotNull(result.getId());
        verify(userMapper).insert(any(User.class));
    }

    @Test
    void testUpdate() {
        User existingUser = createUser(1L, "张三", "zhangsan@test.com");
        when(userMapper.findById(1L)).thenReturn(existingUser);

        User result = userService.update(1L, "张三-new", "new-email@test.com");
        assertEquals("张三-new", result.getName());
        assertEquals("new-email@test.com", result.getEmail());
        verify(userMapper).findById(1L);
        verify(userMapper).update(existingUser);
    }

    @Test
    void testUpdateNotFound() {
        when(userMapper.findById(99L)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> userService.update(99L, "xx", "xx@test.com"));
        verify(userMapper).findById(99L);
        verify(userMapper, never()).update(any());
    }

    @Test
    void testDeleteById() {
        userService.deleteById(1L);
        verify(userMapper).deleteById(1L);
    }

    private User createUser(Long id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}
