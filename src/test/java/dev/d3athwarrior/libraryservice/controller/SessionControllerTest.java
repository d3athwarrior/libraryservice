package dev.d3athwarrior.libraryservice.controller;

import dev.d3athwarrior.libraryservice.service.UserService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(SessionController.class)
public class SessionControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void givenThereAreUsersInTheSystem_whenAUserTriesToLogin_thenReturnTheUserIdIfTheUserIsValid() throws Exception {
        given(userService.validateUser(anyLong())).willReturn(1L);
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("1"))
                .andExpect(jsonPath("$", Matchers.is(1)));
    }

    @Test
    void givenThereAreUsersInTheSystem_whenAnInvalidUserAttemptsLogin_thenReturnNegativeNumber() throws Exception {
        given(userService.validateUser(anyLong())).willReturn(-1L);
        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content("1"))
                .andExpect(jsonPath("$", Matchers.is(-1)));
    }
}
