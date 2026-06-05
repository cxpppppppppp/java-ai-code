package com.ai.code.service;

import com.ai.code.entity.User;
import com.ai.code.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public List<User> findAll() {
        return userMapper.findAll();
    }

    public User findById(Long id) {
        return userMapper.findById(id);
    }

    public User findByEmail(String email) {
        return userMapper.findByEmail(email);
    }

    public User create(String name, String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        userMapper.insert(user);
        return user;
    }

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

    public void deleteById(Long id) {
        userMapper.deleteById(id);
    }
}
