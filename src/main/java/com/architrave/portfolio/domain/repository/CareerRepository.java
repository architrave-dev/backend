package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.Career;
import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.enumType.CareerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface CareerRepository extends JpaRepository<Career, Long> {
    List<Career> findByMemberOrderByIndexAsc(Member member);

    @Modifying
    @Query("""
            DELETE FROM Career c
            WHERE c.member = :member
            """)
    void deleteByMember(@Param("member") Member member);

    @Query("SELECT c " +
            "FROM Career c " +
            "WHERE c.member = :member " +
            "AND c.careerType = :careerType " +
            "ORDER BY c.index ASC")
    List<Career> findByMemberAndCareerType(
            @Param("member") Member member,
            @Param("careerType") CareerType careerType
    );
}
