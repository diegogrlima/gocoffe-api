package github.com.diegogrlima.gocoffe.application.dto.auth;

import github.com.diegogrlima.gocoffe.domain.user.entity.Role;

public record LoginOutput(
        String token,
        String email,
        Role role
) {
}
