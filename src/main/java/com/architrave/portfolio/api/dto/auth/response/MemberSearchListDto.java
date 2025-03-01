package com.architrave.portfolio.api.dto.auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberSearchListDto {
    private List<MemberSearchDto> memberSearchList;
}
