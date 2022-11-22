package colfume.domain.member.controller;

import colfume.common.dto.ErrorResponseDto;
import colfume.common.dto.Response;
import colfume.common.enums.ErrorCode;
import colfume.domain.member.dto.request.MemberRequestDto;
import colfume.domain.member.dto.request.PasswordRequestDto;
import colfume.domain.member.service.MemberService;
import colfume.oauth.model.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
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

    @Secured("ROLE_ADMIN")
    @GetMapping
    public Response getMemberList() {
        return Response.success(memberService.findMemberDtoList(), HttpStatus.OK);
    }

    @PostMapping("/join")
    public Response join(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        return Response.success(memberService.join(memberRequestDto), HttpStatus.CREATED);
    }

    @Secured("ROLE_USER")
    @PatchMapping("/password")
    public Response updatePassword(@AuthenticationPrincipal UserPrincipal userPrincipal, @Valid @RequestBody PasswordRequestDto passwordRequestDto) {
        memberService.updatePassword(userPrincipal.getId(), passwordRequestDto.getPassword(), passwordRequestDto.getNewPw());
        return Response.success(HttpStatus.CREATED);
    }

    @Secured("ROLE_USER")
    @PatchMapping("/name")
    public Response updateInfo(@AuthenticationPrincipal UserPrincipal userPrincipal, @RequestParam(required = false) String name, @RequestParam(required = false) String imageUrl) {
        if (!StringUtils.hasText(name)) {
            ErrorResponseDto error = new ErrorResponseDto(ErrorCode.NAME_NOT_INSERTED);
            return Response.failure(-1000, error, HttpStatus.valueOf(error.getStatus()));
        }
        memberService.updateInfo(userPrincipal.getId(), name, imageUrl);
        return Response.success(HttpStatus.CREATED);
    }

    @Secured("ROLE_USER")
    @DeleteMapping
    public Response withdraw(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        memberService.withdraw(userPrincipal.getId());
        return Response.success(HttpStatus.NO_CONTENT);
    }
}