package colfume.domain.member.model.repository;

import colfume.domain.member.model.entity.MailCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailCodeRepository extends JpaRepository<MailCode, Long> {

    Optional<MailCode> findByEmail(String email);
}