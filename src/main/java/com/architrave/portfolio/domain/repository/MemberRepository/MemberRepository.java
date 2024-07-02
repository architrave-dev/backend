package com.architrave.portfolio.domain.repository.MemberRepository;

import com.architrave.portfolio.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByEmail(String email);
}
