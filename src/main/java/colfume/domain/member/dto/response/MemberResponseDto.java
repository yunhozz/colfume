package colfume.domain.member.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {

    private Long id;
    private String email;
    private String password;
    private String name;
    private String imageUrl;
    private String role;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}