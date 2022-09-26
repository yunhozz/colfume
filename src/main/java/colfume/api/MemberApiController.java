package colfume.api;

import colfume.domain.member.service.MemberService;
import colfume.oauth.UserPrincipal;
import colfume.dto.ErrorResponseDto;
import colfume.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static colfume.dto.MemberDto.*;
import static colfume.dto.TokenDto.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/members/{userId}")
    public ResponseEntity<MemberResponseDto> getMember(@PathVariable String userId) {
        return ResponseEntity.ok(memberService.findMemberDto(Long.valueOf(userId)));
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberResponseDto>> getMemberList() {
        return ResponseEntity.ok(memberService.findMemberDtoList());
    }

    @PostMapping("/members/join")
    public ResponseEntity<Long> join(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        return new ResponseEntity<>(memberService.join(memberRequestDto), HttpStatus.CREATED);
    }

    @PostMapping("/members/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return new ResponseEntity<>(memberService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword()), HttpStatus.CREATED);
    }

    @PostMapping("/members/reissue")
    public ResponseEntity<TokenResponseDto> tokenReissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(memberService.tokenReissue(tokenRequestDto), HttpStatus.CREATED);
    }

    @PatchMapping("/members/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal UserPrincipal user, @Valid @RequestBody PasswordRequestDto passwordRequestDto) {
        memberService.updatePassword(user.getId(), passwordRequestDto.getPassword(), passwordRequestDto.getNewPw());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/members/name")
    public ResponseEntity<Object> updateInfo(@AuthenticationPrincipal UserPrincipal user, @RequestParam(required = false) String name, @RequestParam(required = false) String imageUrl) {
        if (!StringUtils.hasText(name)) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(ErrorCode.NAME_NOT_INSERTED));
        }
        memberService.updateInfo(user.getId(), name, imageUrl);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/members")
    public ResponseEntity<Void> withdraw(@AuthenticationPrincipal UserPrincipal userDetails) {
        memberService.withdraw(userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}