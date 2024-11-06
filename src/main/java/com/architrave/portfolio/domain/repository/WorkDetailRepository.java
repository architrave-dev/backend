package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.WorkDetail;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

@Trace
public interface WorkDetailRepository extends JpaRepository<WorkDetail, Long> {
    List<WorkDetail> findByWork(Work work);
    void deleteByWork(Work work);
}
