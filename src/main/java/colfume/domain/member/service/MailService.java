package colfume.domain.member.service;

import colfume.domain.member.model.entity.ConfirmationToken;
import colfume.domain.member.model.entity.MailCode;
import colfume.domain.member.model.repository.ConfirmationTokenRepository;
import colfume.domain.member.model.repository.MailCodeRepository;
import colfume.domain.member.service.exception.ConfirmationTokenNotFoundException;
import colfume.domain.member.service.exception.EmailNotFoundException;
import colfume.domain.member.service.exception.EmailNotVerifiedException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;
    private final MailCodeRepository mailCodeRepository;
    private final ConfirmationTokenRepository confirmationTokenRepository;

    private static final long EMAIL_TOKEN_EXPIRATION_TIME_VALUE = 5L; // 5분 후 만료

    @Transactional
    public void sendLinkMail(Long userId, String email) {
        ConfirmationToken confirmationToken = new ConfirmationToken(userId, LocalDateTime.now().plusMinutes(EMAIL_TOKEN_EXPIRATION_TIME_VALUE));
        confirmationTokenRepository.save(confirmationToken);

        MimeMessage message = createMessageWithLink(confirmationToken.getId(), email);
        javaMailSender.send(message);
    }

    @Transactional
    public void sendCodeMail(String email) {
        MimeMessage message = createMessageWithCode(email);
        javaMailSender.send(message);
    }

    // 회원가입 완료 후 링크를 통해 인증
    @Transactional
    public void confirmEmail(Long tokenId) {
        ConfirmationToken confirmationToken = findByIdAndExpirationDateAfterAndExpired(tokenId);
        confirmationToken.useTokenAndVerify(); // 토큰 만료, 이메일 인증 성공 처리
    }

    // 회원가입 도중 이메일로 전송된 코드로 인증
    @Transactional
    public void confirmCode(String email, String code) {
        MailCode mailCode = mailCodeRepository.findByEmail(email)
                .orElseThrow(EmailNotFoundException::new);

        if (mailCode.isCodeNotEqualsWith(code)) {
            throw new EmailNotVerifiedException();
        }

        mailCode.verify(); // 코드 검증 성공
    }

    // 링크 인증 메일 생성
    private MimeMessage createMessageWithLink(Long confirmationTokenId, String email) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            message.addRecipients(Message.RecipientType.TO, email); // 수신 이메일 설정
            message.setSubject("회원가입 인증 메일입니다."); // 제목 설정
            message.setText("<a href=\"localhost:8080/api/mail/link/verify?tokenId=" + confirmationTokenId + "\">" + "여기</a>를 눌러 인증을 완료하세요.", "UTF-8", "html"); // 내용 설정
            message.setFrom(new InternetAddress("qkrdbsgh1121@naver.com")); // 송신 이메일 설정

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return message;
    }

    // 코드 인증 메일 생성
    private MimeMessage createMessageWithCode(String email) {
        MimeMessage message = javaMailSender.createMimeMessage();
        String code = createCode();
        MailCode mailCode = new MailCode(email, code, false);
        mailCodeRepository.save(mailCode);

        String msgg = "";
        msgg += "<div style='margin:100px;'>";
        msgg += "<h1> 안녕하세요! Colfume 운영자입니다. </h1>";
        msgg += "<br>";
        msgg += "<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>";
        msgg += "<br>";
        msgg += "<p>감사합니다!<p>";
        msgg += "<br>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += code + "</strong><div><br/> ";
        msgg += "</div>";

        try {
            message.setRecipients(Message.RecipientType.TO, email);
            message.setSubject("회원가입 인증 메일입니다.");
            message.setText(msgg, "UTF-8", "html");
            message.setFrom(new InternetAddress("qkrdbsgh1121@naver.com"));

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }

        return message;
    }

    private String createCode() {
        StringBuilder code = new StringBuilder();
        Random rnd = new Random();

        for (int i = 0; i < 8; i++) { // 인증코드 8자리
            int index = rnd.nextInt(3); // 0~2 까지 랜덤

            switch (index) {
                case 0 -> code.append((char) (rnd.nextInt(26) + 97)); // a~z  (ex. 1+97=98 => (char)98 = 'b')
                case 1 -> code.append((char) (rnd.nextInt(26) + 65)); // A~Z
                case 2 -> code.append((rnd.nextInt(10))); // 0~9
            }
        }

        return code.toString();
    }

    private ConfirmationToken findByIdAndExpirationDateAfterAndExpired(Long confirmationTokenId) {
        return confirmationTokenRepository.findByIdAndExpirationDateAfterAndIsExpired(confirmationTokenId, LocalDateTime.now(), false)
                .orElseThrow(ConfirmationTokenNotFoundException::new);
    }
}