package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.TextBox;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextBoxRepository extends JpaRepository<TextBox, Long> {
}
