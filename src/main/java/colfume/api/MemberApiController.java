package colfume.api;

import colfume.api.dto.Response;
import colfume.domain.member.service.MemberService;
import colfume.oauth.UserPrincipal;
import colfume.dto.ErrorResponseDto;
import colfume.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static colfume.dto.MemberDto.*;
import static colfume.dto.TokenDto.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/members/{userId}")
    public Response getMember(@PathVariable String userId) {
        return Response.success(memberService.findMemberDto(Long.valueOf(userId)), HttpStatus.OK);
    }

    @GetMapping("/members")
    public Response getMemberList() {
        return Response.success(memberService.findMemberDtoList(), HttpStatus.OK);
    }

    @PostMapping("/members/join")
    public Response join(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        return Response.success(memberService.join(memberRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/members/login")
    public Response login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return Response.success(memberService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword()), HttpStatus.CREATED);
    }

    @PostMapping("/members/reissue")
    public Response tokenReissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return Response.success(memberService.tokenReissue(tokenRequestDto), HttpStatus.CREATED);
    }

    @PatchMapping("/members/password")
    public Response updatePassword(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody PasswordRequestDto passwordRequestDto) {
        memberService.updatePassword(user.getId(), passwordRequestDto.getPassword(), passwordRequestDto.getNewPw());
        return Response.success(HttpStatus.CREATED);
    }

    @PatchMapping("/members/name")
    public Response updateInfo(@AuthenticationPrincipal UserPrincipal user, @RequestParam(required = false) String name, @RequestParam(required = false) String imageUrl) {
        if (!StringUtils.hasText(name)) {
            ErrorResponseDto error = new ErrorResponseDto(ErrorCode.NAME_NOT_INSERTED);
            return Response.failure(-1000, error.getMessage(), error, HttpStatus.BAD_REQUEST);
        }
        memberService.updateInfo(user.getId(), name, imageUrl);
        return Response.success(HttpStatus.CREATED);
    }

    @DeleteMapping("/members")
    public Response withdraw(@AuthenticationPrincipal UserPrincipal userDetails) {
        memberService.withdraw(userDetails.getId());
        return Response.success(HttpStatus.NO_CONTENT);
    }
}