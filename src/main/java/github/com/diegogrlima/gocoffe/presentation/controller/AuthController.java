package github.com.diegogrlima.gocoffe.presentation.controller;

import github.com.diegogrlima.gocoffe.application.dto.auth.LoginInput;
import github.com.diegogrlima.gocoffe.application.dto.auth.LoginOutput;
import github.com.diegogrlima.gocoffe.application.dto.auth.LogoutOutput;
import github.com.diegogrlima.gocoffe.domain.auth.usecase.AuthenticateUserUseCase;
import github.com.diegogrlima.gocoffe.domain.auth.usecase.LogoutUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticateUserUseCase authenticateUserUseCase;
    private final LogoutUseCase logoutUseCase;

    @PostMapping("/login")
    public ResponseEntity<LoginOutput> login(@Valid @RequestBody LoginInput input) {
        LoginOutput output = authenticateUserUseCase.execute(input);
        return ResponseEntity.ok(output);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @org.springframework.web.bind.annotation.RequestHeader(
                    value = "Authorization", required = false) String authorization) {
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(
                    java.util.Map.of("message", "Authorization token is required")
            );
        }

        String token = authorization.substring(7);
        LogoutOutput output = logoutUseCase.execute(token);
        return ResponseEntity.ok(output);
    }
}
