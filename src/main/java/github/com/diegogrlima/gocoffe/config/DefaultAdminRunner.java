package github.com.diegogrlima.gocoffe.config;

import github.com.diegogrlima.gocoffe.domain.user.usecase.CreateDefaultAdminUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultAdminRunner implements CommandLineRunner {

    private final CreateDefaultAdminUseCase createDefaultAdminUseCase;

    @Override
    public void run(String... args) {
        try {
            createDefaultAdminUseCase.execute();
            log.info("Default admin user created successfully");
        } catch (RuntimeException e) {
            log.info("Admin user already exists, skipping creation");
        }
    }
}
