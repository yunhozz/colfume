package colfume.api;

import colfume.api.dto.Response;
import colfume.api.dto.member.LoginRequestDto;
import colfume.api.dto.member.MemberRequestDto;
import colfume.api.dto.member.PasswordRequestDto;
import colfume.common.ErrorResponseDto;
import colfume.common.enums.ErrorCode;
import colfume.domain.member.service.MemberService;
import colfume.domain.member.service.dto.TokenResponseDto;
import colfume.oauth.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static colfume.oauth.handler.OAuth2AuthorizationRequestRepository.REFRESH_TOKEN;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/{id}")
    public Response getMember(@PathVariable String id) {
        return Response.success(memberService.findMemberDto(Long.valueOf(id)), HttpStatus.OK);
    }

    @GetMapping
    public Response getMemberList() {
        return Response.success(memberService.findMemberDtoList(), HttpStatus.OK);
    }

    @GetMapping("/token/reissue")
    public Response tokenReissue(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = request.getHeader("Refresh");
        TokenResponseDto tokenResponseDto = null;

        if (refreshToken != null) {
            tokenResponseDto = memberService.tokenReissue(refreshToken);
            addAccessTokenOnResponse(response, tokenResponseDto);
            addRefreshTokenOnCookie(request, response, tokenResponseDto);
        }
        return Response.success(tokenResponseDto, HttpStatus.OK);
    }

    @PostMapping("/join")
    public Response join(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        return Response.success(memberService.join(memberRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public Response login(@Valid @RequestBody LoginRequestDto loginRequestDto, HttpServletRequest request, HttpServletResponse response) {
        TokenResponseDto tokenResponseDto = memberService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());
        addAccessTokenOnResponse(response, tokenResponseDto);
        addRefreshTokenOnCookie(request, response, tokenResponseDto);

        return Response.success(tokenResponseDto, HttpStatus.CREATED);
    }

    @PatchMapping("/password")
    public Response updatePassword(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody PasswordRequestDto passwordRequestDto) {
        memberService.updatePassword(userPrincipal.getId(), passwordRequestDto.getPassword(), passwordRequestDto.getNewPw());
        return Response.success(HttpStatus.CREATED);
    }

    @PatchMapping("/name")
    public Response updateInfo(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam(required = false) String name, @RequestParam(required = false) String imageUrl) {
        if (!StringUtils.hasText(name)) {
            ErrorResponseDto error = new ErrorResponseDto(ErrorCode.NAME_NOT_INSERTED);
            return Response.failure(-1000, error, HttpStatus.BAD_REQUEST);
        }
        memberService.updateInfo(userPrincipal.getId(), name, imageUrl);
        return Response.success(HttpStatus.CREATED);
    }

    @DeleteMapping
    public Response withdraw(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        memberService.withdraw(userPrincipal.getId());
        return Response.success(HttpStatus.NO_CONTENT);
    }

    // 헤더에 jwt access 토큰 추가
    private void addAccessTokenOnResponse(HttpServletResponse response, TokenResponseDto tokenResponseDto) {
        response.setContentType("application/json;charset=UTF-8");
        response.addHeader("Authorization", tokenResponseDto.getGrantType() + tokenResponseDto.getAccessToken());
    }

    // 쿠키에 jwt refresh 토큰 추가
    private void addRefreshTokenOnCookie(HttpServletRequest request, HttpServletResponse response, TokenResponseDto tokenResponseDto) {
        CookieUtils.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtils.addCookie(response, REFRESH_TOKEN, tokenResponseDto.getRefreshToken(), (int) tokenResponseDto.getRefreshTokenExpireDate() / 3600);
    }
}