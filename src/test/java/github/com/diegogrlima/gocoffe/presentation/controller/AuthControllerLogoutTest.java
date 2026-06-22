package github.com.diegogrlima.gocoffe.presentation.controller;

import github.com.diegogrlima.gocoffe.application.dto.auth.LogoutOutput;
import github.com.diegogrlima.gocoffe.config.GlobalExceptionHandler;
import github.com.diegogrlima.gocoffe.domain.auth.usecase.LogoutUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerLogoutTest {

    @Mock
    private LogoutUseCase logoutUseCase;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void shouldReturn200WhenLogoutSuccessfully() throws Exception {
        LogoutOutput output = new LogoutOutput("Logged out successfully");

        when(logoutUseCase.execute("valid-token")).thenReturn(output);

        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logged out successfully"));
    }

    @Test
    void shouldReturn400WhenTokenIsInvalid() throws Exception {
        when(logoutUseCase.execute("invalid-token"))
                .thenThrow(new RuntimeException("Invalid or expired token"));

        mockMvc.perform(post("/auth/logout")
                        .header("Authorization", "Bearer invalid-token"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid or expired token"));
    }

    @Test
    void shouldReturn400WhenNoTokenProvided() throws Exception {
        mockMvc.perform(post("/auth/logout"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Authorization token is required"));
    }
}
