package colfume.common.converter.entity;

import colfume.api.dto.member.MemberRequestDto;
import colfume.common.enums.Role;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.service.dto.MemberResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberConverter implements EntityConverter<Member, MemberRequestDto, MemberResponseDto> {

    private final BCryptPasswordEncoder encoder;

    @Override
    public Member convertToEntity(MemberRequestDto memberRequestDto) {
        return Member.builder()
                .email(memberRequestDto.getEmail())
                .password(encoder.encode(memberRequestDto.getPassword()))
                .name(memberRequestDto.getName())
                .imageUrl(memberRequestDto.getImageUrl())
                .role(Role.USER)
                .build();
    }

    @Override
    public MemberResponseDto convertToDto(Member member) {
        return new MemberResponseDto(member.getId(), member.getEmail(), member.getPassword(), member.getName(), member.getImageUrl(), member.getCreatedDate(), member.getLastModifiedDate());
    }
}