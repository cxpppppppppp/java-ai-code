package com.ai.code.controller;

import com.ai.code.entity.User;
import com.ai.code.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void testList() throws Exception {
        User user = createUser(1L, "张三", "zhangsan@test.com");
        when(userService.findAll()).thenReturn(List.of(user));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("张三"))
                .andExpect(jsonPath("$[0].email").value("zhangsan@test.com"));
    }

    @Test
    void testGetByIdFound() throws Exception {
        User user = createUser(1L, "张三", "zhangsan@test.com");
        when(userService.findById(1L)).thenReturn(user);

        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("张三"));
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        when(userService.findById(99L)).thenReturn(null);

        mockMvc.perform(get("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate() throws Exception {
        User createdUser = createUser(3L, "王五", "wangwu@test.com");
        when(userService.create("王五", "wangwu@test.com")).thenReturn(createdUser);

        Map<String, String> body = Map.of("name", "王五", "email", "wangwu@test.com");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(3L));
    }

    @Test
    void testCreateBadRequest() throws Exception {
        Map<String, String> body = Map.of("name", "无邮箱");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testUpdate() throws Exception {
        User updatedUser = createUser(1L, "张三-new", "new@test.com");
        when(userService.update(1L, "张三-new", "new@test.com")).thenReturn(updatedUser);

        Map<String, String> body = Map.of("name", "张三-new", "email", "new@test.com");

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("张三-new"))
                .andExpect(jsonPath("$.email").value("new@test.com"));
    }

    @Test
    void testUpdateNotFound() throws Exception {
        when(userService.update(anyLong(), anyString(), anyString()))
                .thenThrow(new RuntimeException("User not found: 99"));

        Map<String, String> body = Map.of("name", "xx", "email", "xx@test.com");

        mockMvc.perform(put("/api/users/99")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDelete() throws Exception {
        when(userService.findById(1L)).thenReturn(createUser(1L, "张三", "zhangsan@test.com"));

        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteNotFound() throws Exception {
        when(userService.findById(99L)).thenReturn(null);

        mockMvc.perform(delete("/api/users/99"))
                .andExpect(status().isNotFound());
    }

    private User createUser(Long id, String name, String email) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}
