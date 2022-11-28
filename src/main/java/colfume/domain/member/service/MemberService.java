package colfume.domain.member.service;

import colfume.domain.member.dto.MemberResponseDto;
import colfume.domain.member.dto.request.MemberRequestDto;
import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.repository.MemberRepository;
import colfume.domain.member.service.exception.EmailDuplicateException;
import colfume.domain.member.service.exception.MemberNotFoundException;
import colfume.domain.member.service.exception.PasswordMismatchException;
import colfume.domain.member.service.exception.PasswordSameException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Long join(MemberRequestDto memberRequestDto) {
        if (memberRepository.findAll().stream().anyMatch(m -> m.isEmailEqualsWith(memberRequestDto.getEmail()))) {
            throw new EmailDuplicateException();
        }

        MemberConverter converter = new MemberConverter();
        Member member = converter.convertToEntity(memberRequestDto);

        return memberRepository.save(member).getId();
    }

    @Transactional
    public void updatePassword(Long userId, String password, String newPw) {
        if (password.equals(newPw)) {
            throw new PasswordSameException();
        }

        Member member = findMember(userId);

        if (member.isPasswordNotEqualsWith(password)) {
            throw new PasswordMismatchException();
        }

        member.updatePassword(newPw);
    }

    @Transactional
    public void updateInfo(Long userId, String name, String imageUrl) {
        Member member = findMember(userId);
        member.updateInfo(name, imageUrl);
    }

    @Transactional
    public void withdraw(Long userId) {
        Member member = findMember(userId);
        memberRepository.delete(member);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findMemberDto(Long userId) {
        Member member = findMember(userId);
        MemberConverter converter = new MemberConverter();

        return converter.convertToDto(member);
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findMemberDtoList() {
        MemberConverter converter = new MemberConverter();
        return memberRepository.findAll().stream()
                .map(converter::convertToDto)
                .collect(Collectors.toList());
    }

    private Member findMember(Long userId) {
        return memberRepository.findById(userId)
                .orElseThrow(MemberNotFoundException::new);
    }
}