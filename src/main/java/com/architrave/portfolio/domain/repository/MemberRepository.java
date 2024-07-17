package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByEmail(String email);

    Optional<Member> findByAui(String aui);

    Optional<Member> findByEmail(String email);
}
