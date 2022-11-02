package colfume.domain.member.service.dto;

import colfume.domain.member.model.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class MemberResponseDto {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String imageUrl;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public MemberResponseDto(Member member) {
        id = member.getId();
        email = member.getEmail();
        password = member.getPassword();
        name = member.getName();
        imageUrl = member.getImageUrl();
        createdDate = member.getCreatedDate();
        lastModifiedDate = member.getLastModifiedDate();
    }
}