package github.com.diegogrlima.gocoffe.domain.auth.usecase;

import github.com.diegogrlima.gocoffe.application.dto.auth.LoginInput;
import github.com.diegogrlima.gocoffe.application.dto.auth.LoginOutput;
import github.com.diegogrlima.gocoffe.domain.user.entity.Role;
import github.com.diegogrlima.gocoffe.domain.user.entity.User;
import github.com.diegogrlima.gocoffe.domain.user.repository.UserRepository;
import github.com.diegogrlima.gocoffe.infrastructure.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticateUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthenticateUserUseCase authenticateUserUseCase;

    @Test
    void shouldAuthenticateUserAndReturnToken() {
        LoginInput input = new LoginInput("diego@test.com", "123456");

        User user = User.builder()
                .email("diego@test.com")
                .password("$2a$10$encodedPassword")
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail("diego@test.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("123456", "$2a$10$encodedPassword"))
                .thenReturn(true);
        when(jwtService.generateToken(any(User.class)))
                .thenReturn("valid-jwt-token");

        LoginOutput result = authenticateUserUseCase.execute(input);

        assertNotNull(result);
        assertEquals("valid-jwt-token", result.token());
        assertEquals("diego@test.com", result.email());
        assertEquals(Role.USER, result.role());
    }

    @Test
    void shouldThrowExceptionWhenEmailNotFound() {
        LoginInput input = new LoginInput("wrong@test.com", "123456");

        when(userRepository.findByEmail("wrong@test.com"))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authenticateUserUseCase.execute(input));

        assertEquals("Invalid credentials", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenPasswordIsWrong() {
        LoginInput input = new LoginInput("diego@test.com", "wrongpassword");

        User user = User.builder()
                .email("diego@test.com")
                .password("$2a$10$encodedPassword")
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail("diego@test.com"))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongpassword", "$2a$10$encodedPassword"))
                .thenReturn(false);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authenticateUserUseCase.execute(input));

        assertEquals("Invalid credentials", exception.getMessage());
    }
}
