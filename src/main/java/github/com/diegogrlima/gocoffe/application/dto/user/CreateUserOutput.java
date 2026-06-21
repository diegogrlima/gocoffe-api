package github.com.diegogrlima.gocoffe.application.dto.user;

import github.com.diegogrlima.gocoffe.domain.user.entity.Role;

import java.util.UUID;

public record CreateUserOutput(
        UUID id,
        String name,
        String email,
        Role role
) {
}
