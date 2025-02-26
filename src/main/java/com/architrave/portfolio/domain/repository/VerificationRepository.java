package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.Verification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationRepository extends JpaRepository<Verification, Long> {

    Optional<Verification> findByKey(String key);
}
