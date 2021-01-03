package com.example.demo.adapter.controller.http;

import com.example.demo.app.messenger.UserMessenger;
import com.example.demo.app.usecase.UserUsecase;
import com.example.demo.domain.entity.User;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.helper.UserAggregatorTestHelper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@EnableWebMvc
class UserControllerTest {

    private static MockMvc mockMvc;
    private final WebApplicationContext webApplicationContext;
    private final ObjectMapper objectMapper;

    @MockBean
    UserUsecase userUsecase;

    @MockBean
    UserMessenger userMessenger;

    @Autowired
    public UserControllerTest(WebApplicationContext webApplicationContext,
    ObjectMapper objectMapper) {
        this.webApplicationContext = webApplicationContext;
        this.objectMapper = objectMapper;
    }

    @BeforeEach
    void createMockMvc() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext)
                .build();
    }

    @Test
    @DisplayName("empty users list returned")
    void getUser() throws Exception {
        List<User> expectedUsers = new ArrayList<>();
        when(userUsecase.getUser()).thenReturn(expectedUsers);

        MvcResult mvcResult = mockMvc.perform(get("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        List<User> actualUsers = objectMapper.readValue(mvcResult.getResponse().getContentAsString(),
                new TypeReference<>() {});
        assertEquals(expectedUsers, actualUsers);
    }

    @ParameterizedTest
    @DisplayName("get user by id controller test")
    @CsvSource({
            "0,sample,18",
            "1,,18",
            "2,sample,",
            "3,,",
    })
    void getOneUser(@AggregateWith(UserAggregatorTestHelper.class) User expectedUser) throws Exception {
        when(userUsecase.getUserById(expectedUser.getId().toString())).thenReturn(expectedUser);

        MvcResult mvcResult = mockMvc.perform(get("/users/" + expectedUser.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();

        User actualUser = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        assertEquals(expectedUser, actualUser);
    }

    @Test
    @DisplayName("get user by id but Not Found in controller test")
    void getOneUserNotFound() throws Exception {
        String id = "1";
        doThrow(ResourceNotFoundException.class).when(userUsecase).getUserById(id);

        mockMvc.perform(get("/users/" + id)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @ParameterizedTest
    @DisplayName("create user controller test")
    @CsvSource({
            "0,sample,18",
            "1,,18",
            "2,sample,",
            "3,,",
    })
    void createUser(@AggregateWith(UserAggregatorTestHelper.class) User expectedUser) throws Exception {
        when(userUsecase.createUser(expectedUser)).thenReturn(expectedUser);

        MvcResult mvcResult = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expectedUser)))
                .andExpect(status().isOk()).andReturn();

        User actualUser = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), User.class);
        assertEquals(expectedUser, actualUser);
    }

    @ParameterizedTest
    @DisplayName("delete user controller test")
    @ValueSource(strings = {"1"})
    void deleteUser(String id) throws Exception {
        doNothing().when(userUsecase).deleteUserById(id);

        mockMvc.perform(delete("/users/" + id))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("delete user not found then controller returning 404")
    void deleteUserNotFound() throws Exception {
        String id = "1";
        doThrow(new ResourceNotFoundException("not found")).when(userUsecase).deleteUserById(id);

        mockMvc.perform(delete("/users/" + id))
                .andExpect(status().isNotFound());
    }

    @Test
    void postUserToSqs() {
    }

    @Test
    void receiveFromSqs() {
    }
}