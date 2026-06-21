package github.com.diegogrlima.gocoffe.domain.user.repository;

import github.com.diegogrlima.gocoffe.domain.user.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    Optional<User> findById(UUID id);

    Optional<User> findByEmail(String email);

    User save(User user);
}
