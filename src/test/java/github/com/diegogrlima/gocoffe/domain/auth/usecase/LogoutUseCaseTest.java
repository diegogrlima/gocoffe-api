package github.com.diegogrlima.gocoffe.domain.auth.usecase;

import github.com.diegogrlima.gocoffe.application.dto.auth.LogoutOutput;
import github.com.diegogrlima.gocoffe.domain.auth.repository.RevokedTokenRepository;
import github.com.diegogrlima.gocoffe.infrastructure.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LogoutUseCaseTest {

    @Mock
    private RevokedTokenRepository revokedTokenRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private LogoutUseCase logoutUseCase;

    @Test
    void shouldRevokeTokenWhenValid() {
        String token = "valid-jwt-token";
        String email = "diego@test.com";
        String tokenId = "token-id-123";

        when(jwtService.validateToken(token)).thenReturn(email);
        when(jwtService.extractTokenId(token)).thenReturn(tokenId);

        LogoutOutput result = logoutUseCase.execute(token);

        assertNotNull(result);
        assertEquals("Logged out successfully", result.message());
        verify(revokedTokenRepository).revokeToken(tokenId);
    }

    @Test
    void shouldThrowExceptionWhenTokenIsInvalid() {
        String token = "invalid-token";

        when(jwtService.validateToken(token)).thenReturn(null);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> logoutUseCase.execute(token));

        assertEquals("Invalid or expired token", exception.getMessage());
        verify(revokedTokenRepository, never()).revokeToken(anyString());
    }

    @Test
    void shouldThrowExceptionWhenTokenIsNull() {
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> logoutUseCase.execute(null));

        assertEquals("Invalid or expired token", exception.getMessage());
        verify(revokedTokenRepository, never()).revokeToken(anyString());
    }
}
