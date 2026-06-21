package github.com.diegogrlima.gocoffe.infrastructure.repository.user;

import github.com.diegogrlima.gocoffe.domain.user.entity.User;
import github.com.diegogrlima.gocoffe.domain.user.repository.UserRepository;
import github.com.diegogrlima.gocoffe.infrastructure.persistence.user.UserJpaEntity;
import github.com.diegogrlima.gocoffe.infrastructure.persistence.user.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findById(UUID id) {
        return userJpaRepository.findById(id)
                .map(this::toDomainEntity);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                .map(this::toDomainEntity);
    }

    @Override
    public User save(User user) {
        UserJpaEntity jpaEntity = toJpaEntity(user);
        UserJpaEntity savedEntity = userJpaRepository.save(jpaEntity);
        return toDomainEntity(savedEntity);
    }

    private UserJpaEntity toJpaEntity(User user) {
        return UserJpaEntity.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }

    private User toDomainEntity(UserJpaEntity jpaEntity) {
        return User.builder()
                .id(jpaEntity.getId())
                .name(jpaEntity.getName())
                .email(jpaEntity.getEmail())
                .password(jpaEntity.getPassword())
                .role(jpaEntity.getRole())
                .createdAt(jpaEntity.getCreatedAt())
                .build();
    }
}
