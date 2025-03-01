package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.Billboard;
import com.architrave.portfolio.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BillboardRepository extends JpaRepository<Billboard, Long> {
    Optional<Billboard> findByMember(Member member);

    @Modifying
    @Query("""
            DELETE FROM Billboard b
            WHERE b.member = :member
            """)
    void deleteByMember(@Param("member") Member member);
}
