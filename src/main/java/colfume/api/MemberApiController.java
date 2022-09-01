package colfume.api;

import colfume.domain.member.service.MemberService;
import colfume.dto.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
        return ResponseEntity.ok(memberService.login(loginRequestDto));
    }
}
