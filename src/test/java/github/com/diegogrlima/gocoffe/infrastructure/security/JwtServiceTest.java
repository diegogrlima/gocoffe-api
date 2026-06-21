package github.com.diegogrlima.gocoffe.infrastructure.security;

import github.com.diegogrlima.gocoffe.domain.user.entity.Role;
import github.com.diegogrlima.gocoffe.domain.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Test
    void shouldGenerateValidToken() {
        ReflectionTestUtils.setField(jwtService, "secret", "test-secret-key-for-jwt-service-unit-test-12345678");

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("diego@test.com")
                .role(Role.ADMIN)
                .build();

        String token = jwtService.generateToken(user);

        assertNotNull(token);
        assertNotNull(!token.isEmpty());
    }

    @Test
    void shouldValidateTokenAndReturnEmail() {
        ReflectionTestUtils.setField(jwtService, "secret", "test-secret-key-for-jwt-service-unit-test-12345678");

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("diego@test.com")
                .role(Role.ADMIN)
                .build();

        String token = jwtService.generateToken(user);
        String email = jwtService.validateToken(token);

        assertEquals("diego@test.com", email);
    }

    @Test
    void shouldReturnNullWhenTokenIsInvalid() {
        ReflectionTestUtils.setField(jwtService, "secret", "test-secret-key-for-jwt-service-unit-test-12345678");

        String email = jwtService.validateToken("invalid.token.here");

        assertNull(email);
    }

    @Test
    void shouldReturnNullWhenTokenWasGeneratedWithDifferentSecret() {
        ReflectionTestUtils.setField(jwtService, "secret", "test-secret-key-for-jwt-service-unit-test-12345678");

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("diego@test.com")
                .role(Role.USER)
                .build();

        String token = jwtService.generateToken(user);

        ReflectionTestUtils.setField(jwtService, "secret", "different-secret-key-for-testing-purposes-12345");

        String email = jwtService.validateToken(token);

        assertNull(email);
    }
}
