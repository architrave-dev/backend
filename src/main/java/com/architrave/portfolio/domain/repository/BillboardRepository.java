package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.Billboard;
import com.architrave.portfolio.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillboardRepository extends JpaRepository<Billboard, Long> {
    Optional<Billboard> findByMember(Member member);
}
