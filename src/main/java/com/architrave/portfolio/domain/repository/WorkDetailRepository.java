package com.architrave.portfolio.domain.repository;

import com.architrave.portfolio.api.dto.work.response.WorkDetailSimpleDto;
import com.architrave.portfolio.domain.model.Work;
import com.architrave.portfolio.domain.model.WorkDetail;
import com.architrave.portfolio.global.aop.logTrace.Trace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

@Trace
public interface WorkDetailRepository extends JpaRepository<WorkDetail, Long> {
    List<WorkDetail> findByWork(Work work);
    void deleteByWork(Work work);
    @Query("Select new com.architrave.portfolio.api.dto.work.response.WorkDetailSimpleDto(wd.id, wd.uploadFile.originUrl) " +
            "FROM WorkDetail wd WHERE wd.work = :work")
    List<WorkDetailSimpleDto> findSimpleByWork(@Param("work") Work work);
}
