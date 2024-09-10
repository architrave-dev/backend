package com.architrave.portfolio.api.dto.career.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedCareerListReq {

    private List<CreateCareerReq> createCareerReqList;
    private List<UpdateCareerReq> updateCareerReqList;
    private List<RemoveCareerReq> removeCareerReqList;
}
