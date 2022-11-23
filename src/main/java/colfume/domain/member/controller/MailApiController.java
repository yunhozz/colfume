package colfume.domain.member.controller;

import colfume.common.dto.Response;
import colfume.domain.member.service.MailService;
import colfume.oauth.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mail")
@RequiredArgsConstructor
public class MailApiController {

    private final MailService mailService;

    // 링크 인증 메일 전송
    @PostMapping("/link")
    public Response sendLinkMail(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        mailService.sendLinkMail(userPrincipal.getId(), userPrincipal.getEmail());
        return Response.success(HttpStatus.CREATED);
    }

    // 링크 인증 검증
    @GetMapping("/link/verify")
    public Response verifyLinkMail(@RequestParam String tokenId) {
        mailService.confirmEmail(Long.valueOf(tokenId));
        return Response.success(HttpStatus.CREATED);
    }

    // 코드 인증 메일 전송
    @PostMapping("/code")
    public Response sendCodeMail(@RequestParam String email) {
        mailService.sendCodeMail(email);
        return Response.success(HttpStatus.CREATED);
    }

    // 코드 인증 검증
    @PatchMapping("/code/verify")
    public Response verifyCodeMail(@RequestParam String email, @RequestParam String code) {
        mailService.confirmCode(email, code);
        return Response.success(HttpStatus.CREATED);
    }
}