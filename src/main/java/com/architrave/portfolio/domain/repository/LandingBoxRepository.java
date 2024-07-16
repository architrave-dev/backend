package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.LandingBox;
import com.architrave.portfolio.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LandingBoxRepository extends JpaRepository<LandingBox, Long> {
    Optional<LandingBox> findByMember(Member member);
}
