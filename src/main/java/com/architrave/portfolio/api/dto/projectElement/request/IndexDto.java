package com.architrave.portfolio.api.dto.projectElement.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IndexDto {
    private Long peId; //기존 ProjectElement의 id
    private Long tempPeId; //새로 생긴 ProjectElement의 tempId
}
