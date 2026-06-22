package github.com.diegogrlima.gocoffe.domain.auth.repository;

public interface RevokedTokenRepository {
    void revokeToken(String tokenId);
    boolean isTokenRevoked(String tokenId);
}
