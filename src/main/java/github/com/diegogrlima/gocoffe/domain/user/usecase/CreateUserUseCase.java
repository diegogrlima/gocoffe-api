package github.com.diegogrlima.gocoffe.domain.user.usecase;

import github.com.diegogrlima.gocoffe.application.dto.user.CreateUserInput;
import github.com.diegogrlima.gocoffe.application.dto.user.CreateUserOutput;
import github.com.diegogrlima.gocoffe.domain.user.entity.Role;
import github.com.diegogrlima.gocoffe.domain.user.entity.User;
import github.com.diegogrlima.gocoffe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserOutput execute(CreateUserInput input) {
        userRepository.findByEmail(input.email())
                .ifPresent(user -> {
                    throw new RuntimeException("Registration failed");
                });

        User user = User.builder()
                .name(input.name())
                .email(input.email())
                .password(passwordEncoder.encode(input.password()))
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);

        return new CreateUserOutput(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }
}
