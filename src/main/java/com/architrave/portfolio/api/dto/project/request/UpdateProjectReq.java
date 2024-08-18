package com.architrave.portfolio.api.dto.project.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectReq {
    @NotNull
    private Long id;
    private String originImgUrl;
    private String thumbnailUrl;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String supportedBy;
    private List<CreateProjectInfoReq> createdProjectInfoList = new ArrayList<>();
    private List<UpdateProjectInfoReq> updatedProjectInfoList = new ArrayList<>();
    private List<RemoveProjectInfoReq> removedProjectInfoList = new ArrayList<>();
    private Boolean isDeleted;
}
