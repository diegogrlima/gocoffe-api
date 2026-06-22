package github.com.diegogrlima.gocoffe.infrastructure.persistence.auth;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RevokedTokenJpaRepository extends JpaRepository<RevokedTokenJpaEntity, Long> {
    boolean existsByTokenId(String tokenId);
}
