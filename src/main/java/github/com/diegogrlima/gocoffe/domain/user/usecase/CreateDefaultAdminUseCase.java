package github.com.diegogrlima.gocoffe.domain.user.usecase;

import github.com.diegogrlima.gocoffe.domain.user.entity.Role;
import github.com.diegogrlima.gocoffe.domain.user.entity.User;
import github.com.diegogrlima.gocoffe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateDefaultAdminUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void execute() {
        userRepository.findByEmail("admin@gocoffe.com")
                .ifPresent(user -> {
                    throw new RuntimeException("Admin user already exists");
                });

        User admin = User.builder()
                .name("Admin")
                .email("admin@gocoffe.com")
                .password(passwordEncoder.encode("admin123"))
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);
    }
}
