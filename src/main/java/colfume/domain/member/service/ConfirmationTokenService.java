package colfume.domain.member.service;

import colfume.domain.member.model.entity.ConfirmationToken;
import colfume.domain.member.model.repository.ConfirmationTokenRepository;
import colfume.enums.ErrorCode;
import colfume.exception.ConfirmationTokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;
    private final MailService mailService;

    private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 5L; // 5분 후 만료

    public Long createConfirmationToken(Long userId, String email) {
        ConfirmationToken confirmationToken = new ConfirmationToken(userId, LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE));
        confirmationTokenRepository.save(confirmationToken);
        mailService.sendLinkMail(confirmationToken.getId(), email);

        return confirmationToken.getId();
    }

    @Transactional(readOnly = true)
    public ConfirmationToken findByIdAndExpirationDateAfterAndExpired(Long confirmationTokenId) {
        return confirmationTokenRepository.findByIdAndExpirationDateAfterAndIsExpired(confirmationTokenId, LocalDateTime.now(), false)
                .orElseThrow(() -> new ConfirmationTokenNotFoundException(ErrorCode.CONFIRMATION_TOKEN_NOT_FOUND));
    }
}