package colfume.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
@Transactional
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender javaMailSender;

    public void sendMail(Long confirmationTokenId, String email) {
        MimeMessage message = createMessage(confirmationTokenId, email);
        javaMailSender.send(message);
    }

    private MimeMessage createMessage(Long confirmationTokenId, String email) {
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.addRecipients(Message.RecipientType.TO, email);
            message.setSubject("회원가입 인증 메일입니다.");
            message.setText("localhost:8080/member/confirm-email?tokenId=" + confirmationTokenId);
            message.setFrom(new InternetAddress("qkrdbsgh1121@naver.com"));
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return message;
    }
}