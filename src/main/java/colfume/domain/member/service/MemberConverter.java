package colfume.domain.member.service;

import colfume.common.EntityConverter;
import colfume.common.enums.Role;
import colfume.domain.member.dto.MemberResponseDto;
import colfume.domain.member.dto.request.MemberRequestDto;
import colfume.domain.member.model.entity.Member;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberConverter implements EntityConverter<Member, MemberRequestDto, MemberResponseDto> {

    @Override
    public Member convertToEntity(MemberRequestDto memberRequestDto) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return Member.builder()
                .email(memberRequestDto.getEmail())
                .password(encoder.encode(memberRequestDto.getPassword()))
                .name(memberRequestDto.getName())
                .imageUrl(memberRequestDto.getImageUrl())
                .role(Role.GUEST)
                .build();
    }

    @Override
    public MemberResponseDto convertToDto(Member member) {
        return new MemberResponseDto(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getName(),
                member.getImageUrl(),
                member.getRole().getAuthority(),
                member.getCreatedDate(),
                member.getLastModifiedDate()
        );
    }
}