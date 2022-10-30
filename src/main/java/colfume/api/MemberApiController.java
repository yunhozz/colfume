package colfume.api;

import colfume.api.dto.Response;
import colfume.api.dto.member.LoginRequestDto;
import colfume.api.dto.member.MemberRequestDto;
import colfume.api.dto.member.PasswordRequestDto;
import colfume.api.dto.member.TokenRequestDto;
import colfume.common.dto.ErrorResponseDto;
import colfume.common.enums.ErrorCode;
import colfume.oauth.UserPrincipal;
import colfume.domain.member.service.MemberService;
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

import javax.validation.Valid;

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

    @PostMapping("/join")
    public Response join(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        return Response.success(memberService.join(memberRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public Response login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return Response.success(memberService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword()), HttpStatus.CREATED);
    }

    @PostMapping("/reissue")
    public Response tokenReissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return Response.success(memberService.tokenReissue(tokenRequestDto), HttpStatus.CREATED);
    }

    @PatchMapping("/password")
    public Response updatePassword(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody PasswordRequestDto passwordRequestDto) {
        memberService.updatePassword(user.getId(), passwordRequestDto.getPassword(), passwordRequestDto.getNewPw());
        return Response.success(HttpStatus.CREATED);
    }

    @PatchMapping("/name")
    public Response updateInfo(@AuthenticationPrincipal UserPrincipal user, @RequestParam(required = false) String name, @RequestParam(required = false) String imageUrl) {
        if (!StringUtils.hasText(name)) {
            ErrorResponseDto error = new ErrorResponseDto(ErrorCode.NAME_NOT_INSERTED);
            return Response.failure(-1000, error, HttpStatus.BAD_REQUEST);
        }
        memberService.updateInfo(user.getId(), name, imageUrl);
        return Response.success(HttpStatus.CREATED);
    }

    @DeleteMapping
    public Response withdraw(@AuthenticationPrincipal UserPrincipal user) {
        memberService.withdraw(user.getId());
        return Response.success(HttpStatus.NO_CONTENT);
    }
}