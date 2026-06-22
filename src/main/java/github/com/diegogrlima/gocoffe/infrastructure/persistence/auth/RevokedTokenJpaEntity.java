package github.com.diegogrlima.gocoffe.infrastructure.persistence.auth;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "revoked_tokens")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevokedTokenJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "uuid")
    private UUID id;

    @Column(name = "token_id", nullable = false, unique = true)
    private String tokenId;

    @CreationTimestamp
    @Column(name = "revoked_at", nullable = false, updatable = false)
    private LocalDateTime revokedAt;
}
