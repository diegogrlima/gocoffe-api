package github.com.diegogrlima.gocoffe.domain.auth.usecase;

import github.com.diegogrlima.gocoffe.application.dto.auth.LogoutOutput;
import github.com.diegogrlima.gocoffe.config.exception.AuthenticationException;
import github.com.diegogrlima.gocoffe.domain.auth.repository.RevokedTokenRepository;
import github.com.diegogrlima.gocoffe.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutUseCase {

    private final RevokedTokenRepository revokedTokenRepository;
    private final JwtService jwtService;

    public LogoutOutput execute(String token) {
        String email = jwtService.validateToken(token);

        if (email == null) {
            throw new AuthenticationException("Invalid or expired token");
        }

        String tokenId = jwtService.extractTokenId(token);

        revokedTokenRepository.revokeToken(tokenId);

        return new LogoutOutput("Logged out successfully");
    }
}
