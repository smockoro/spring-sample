package com.example.demo.exception;

import com.example.demo.adapter.controller.http.UserController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class CustomExceptionHandlerTest {
    private static MockMvc mockMvc;

    @MockBean
    UserController userController;

    @BeforeEach
    void createMockMvc() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController)
                .setControllerAdvice(new CustomExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("exception handler test")
    void handleException() throws Exception {
        doThrow(new ResourceNotFoundException("not found"))
                .when(userController).deleteUser(anyString());
        mockMvc.perform(delete("/users/1"))
                .andExpect(status().isNotFound());
    }
}