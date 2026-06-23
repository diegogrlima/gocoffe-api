package github.com.diegogrlima.gocoffe.domain.user.usecase;

import github.com.diegogrlima.gocoffe.config.exception.ResourceAlreadyExistsException;
import github.com.diegogrlima.gocoffe.domain.user.entity.Role;
import github.com.diegogrlima.gocoffe.domain.user.entity.User;
import github.com.diegogrlima.gocoffe.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CreateDefaultAdminUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.name:Admin}")
    private String adminName;

    public void execute() {
        userRepository.findByEmail(adminEmail)
                .ifPresent(user -> {
                    throw new ResourceAlreadyExistsException("Admin user already exists");
                });

        User admin = User.builder()
                .name(adminName)
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .role(Role.ADMIN)
                .build();

        userRepository.save(admin);
    }
}
