package github.com.diegogrlima.gocoffe.presentation.controller;

import github.com.diegogrlima.gocoffe.application.dto.user.CreateUserOutput;
import github.com.diegogrlima.gocoffe.domain.user.entity.Role;
import github.com.diegogrlima.gocoffe.domain.user.usecase.CreateUserUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private CreateUserUseCase createUserUseCase;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void shouldReturn201WhenCreateUser() throws Exception {
        UUID userId = UUID.randomUUID();

        CreateUserOutput output = new CreateUserOutput(
                userId, "Diego", "diego@test.com", Role.USER);

        when(createUserUseCase.execute(any()))
                .thenReturn(output);

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Diego\",\"email\":\"diego@test.com\",\"password\":\"Senha123\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userId.toString()))
                .andExpect(jsonPath("$.name").value("Diego"))
                .andExpect(jsonPath("$.email").value("diego@test.com"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    void shouldReturn400WhenInvalidInput() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\",\"email\":\"\",\"password\":\"123\"}"))
                .andExpect(status().isBadRequest());
    }
}
