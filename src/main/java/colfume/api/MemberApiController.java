package colfume.api;

import colfume.domain.member.service.MemberService;
import colfume.domain.member.service.UserDetailsImpl;
import colfume.dto.ErrorResponseDto;
import colfume.dto.TokenResponseDto;
import colfume.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static colfume.dto.MemberDto.*;

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
        return ResponseEntity.ok(memberService.join(memberRequestDto));
    }

    @PostMapping("/members/login")
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(memberService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
    }

    @PatchMapping("/members/password")
    public ResponseEntity<Void> updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid @RequestBody PasswordRequestDto passwordRequestDto) {
        memberService.updatePassword(userDetails.getId(), passwordRequestDto.getPassword(), passwordRequestDto.getNewPw());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/members/name")
    public ResponseEntity<Object> updateName(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(required = false) String name) {
        if (!StringUtils.hasText(name)) {
            return ResponseEntity.badRequest().body(new ErrorResponseDto(ErrorCode.NAME_NOT_INSERTED));
        }
        memberService.updateName(userDetails.getId(), name);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/members")
    public ResponseEntity<Void> withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        memberService.withdraw(userDetails.getId());
        return ResponseEntity.noContent().build();
    }
}
