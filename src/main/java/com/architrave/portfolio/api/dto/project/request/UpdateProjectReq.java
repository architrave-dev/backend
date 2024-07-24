package com.architrave.portfolio.api.dto.project.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectReq {
    private Long id;
    private String originImgUrl;
    private String thumbnailUrl;
    private String title;
    private String description;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String supportedBy;
    private List<CreateProjectInfoReq> createdProjectInfoList = new ArrayList<>();
    private List<UpdateProjectInfoReq> updatedProjectInfoList = new ArrayList<>();
    private List<RemoveProjectInfoReq> removedProjectInfoList = new ArrayList<>();
    private Boolean isDeleted;
}
