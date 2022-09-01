package colfume.domain.member.model.repository;

import colfume.domain.member.model.entity.MemberAuthority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAuthorityRepository extends JpaRepository<MemberAuthority, Long> {
}
