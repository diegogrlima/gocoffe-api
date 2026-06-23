package github.com.diegogrlima.gocoffe.presentation.controller;

import github.com.diegogrlima.gocoffe.application.dto.user.CreateUserInput;
import github.com.diegogrlima.gocoffe.application.dto.user.CreateUserOutput;
import github.com.diegogrlima.gocoffe.domain.user.usecase.CreateUserUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final CreateUserUseCase createUserUseCase;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CreateUserOutput> create(@Valid @RequestBody CreateUserInput input) {
        CreateUserOutput output = createUserUseCase.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(output);
    }
}
