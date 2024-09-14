package com.architrave.portfolio.api.dto.projectElement.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectElementListDto {
    private String peIndex;
    private List<ProjectElementDto> projectElementList;
}
