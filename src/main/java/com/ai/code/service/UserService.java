package com.ai.code.service;

import com.ai.code.entity.User;
import com.ai.code.mapper.UserMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Cacheable(value = "users", key = "'all'", unless = "#result == null || #result.isEmpty()")
    public List<User> findAll() {
        return userMapper.findAll();
    }

    @Cacheable(value = "users", key = "#id", unless = "#result == null")
    public User findById(Long id) {
        return userMapper.findById(id);
    }

    @Cacheable(value = "users", key = "'email:' + #email", unless = "#result == null")
    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    @CacheEvict(value = "users", allEntries = true)
    public User create(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        userMapper.insert(user);
        return user;
    }

    @Caching(
            put = @CachePut(value = "users", key = "#id"),
            evict = @CacheEvict(value = "users", key = "'all'")
    )
    public User update(Long id, String name, String email) {
        User user = userMapper.findById(id);
        if (user == null) {
            throw new RuntimeException("User not found: " + id);
        }
        user.setName(name);
        user.setEmail(email);
        userMapper.update(user);
        return user;
    }

    @CacheEvict(value = "users", allEntries = true)
    public void deleteById(Long id) {
        userMapper.deleteById(id);
    }
}
