package colfume.domain.member.model.repository;

import colfume.domain.member.model.entity.Member;
import colfume.domain.member.model.entity.MemberAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface MemberAuthorityRepository extends JpaRepository<MemberAuthority, Long> {

    Set<MemberAuthority> findByMember(Member member);
}
