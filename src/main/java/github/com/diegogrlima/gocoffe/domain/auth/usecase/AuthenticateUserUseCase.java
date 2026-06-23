package github.com.diegogrlima.gocoffe.domain.auth.usecase;

import github.com.diegogrlima.gocoffe.application.dto.auth.LoginInput;
import github.com.diegogrlima.gocoffe.application.dto.auth.LoginOutput;
import github.com.diegogrlima.gocoffe.config.exception.AuthenticationException;
import github.com.diegogrlima.gocoffe.domain.user.entity.User;
import github.com.diegogrlima.gocoffe.domain.user.repository.UserRepository;
import github.com.diegogrlima.gocoffe.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginOutput execute(LoginInput input) {
        User user = userRepository.findByEmail(input.email())
                .orElseThrow(() -> new AuthenticationException("Invalid credentials"));

        if (!passwordEncoder.matches(input.password(), user.getPassword())) {
            throw new AuthenticationException("Invalid credentials");
        }

        String token = jwtService.generateToken(user);

        return new LoginOutput(
                token,
                user.getEmail(),
                user.getRole()
        );
    }
}
