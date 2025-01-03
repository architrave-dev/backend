package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SettingRepository extends JpaRepository<Setting, Long> {
    Optional<Setting> findByMember(Member member);
}
