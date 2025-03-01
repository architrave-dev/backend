package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Setting;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SettingRepository extends JpaRepository<Setting, Long> {
    Optional<Setting> findByMember(Member member);

    @Query("SELECT s " +
            "FROM Setting s " +
            "JOIN FETCH s.member m " +
            "WHERE m.username LIKE CONCAT(:username, '%') " +
            "  AND s.pageVisible = true " +
            "ORDER BY m.username ASC")
    List<Setting> findByUsernamePrefixAndPageVisibleTrue(@Param("username") String username, Pageable pageable);

    @Modifying
    @Query("""
            DELETE FROM Setting s
            WHERE s.member = :member
            """)
    void deleteByMember(@Param("member")Member member);
}
