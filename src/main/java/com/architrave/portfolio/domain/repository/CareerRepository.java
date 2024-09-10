package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.Career;
import com.architrave.portfolio.domain.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CareerRepository extends JpaRepository<Career, Long> {

    List<Career> findByMember(Member member);
}
