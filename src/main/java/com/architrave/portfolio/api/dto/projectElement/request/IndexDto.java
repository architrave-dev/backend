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
    private Long id; //기존 객체의 id
    private Long tempId; //새로 생긴 객체의 tempId
}
