package colfume.domain.member.model.repository;

import colfume.domain.member.model.entity.ConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ConfirmationTokenRepository extends JpaRepository<ConfirmationToken, Long> {

    // pk 값, 만료 시간, 만료 여부로 조회
    Optional<ConfirmationToken> findByIdAndExpirationDateAfterAndIsExpired(Long confirmationTokenId, LocalDateTime now, boolean isExpired);
}
