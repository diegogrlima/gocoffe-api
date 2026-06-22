package github.com.diegogrlima.gocoffe.infrastructure.repository.auth;

import github.com.diegogrlima.gocoffe.domain.auth.repository.RevokedTokenRepository;
import github.com.diegogrlima.gocoffe.infrastructure.persistence.auth.RevokedTokenJpaEntity;
import github.com.diegogrlima.gocoffe.infrastructure.persistence.auth.RevokedTokenJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RevokedTokenRepositoryImpl implements RevokedTokenRepository {

    private final RevokedTokenJpaRepository revokedTokenJpaRepository;

    @Override
    public void revokeToken(String tokenId) {
        RevokedTokenJpaEntity entity = RevokedTokenJpaEntity.builder()
                .tokenId(tokenId)
                .build();
        revokedTokenJpaRepository.save(entity);
    }

    @Override
    public boolean isTokenRevoked(String tokenId) {
        return revokedTokenJpaRepository.existsByTokenId(tokenId);
    }
}
