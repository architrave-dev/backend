package com.architrave.portfolio.api.dto.reorder.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateReorderListReq {
    private String id;
    //projectInfo, projectElement => projectId
    //project => aui
    //career => 구분값...?
    //detail => workId
    private List<ReorderReq> reorderReqList;
}
