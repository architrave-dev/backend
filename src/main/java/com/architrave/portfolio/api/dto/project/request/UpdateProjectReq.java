package com.architrave.portfolio.api.dto.project.request;

import com.architrave.portfolio.api.dto.uploadFile.request.UpdateUploadFileReq;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProjectReq {
    @NotNull
    private Long id;
    private UpdateUploadFileReq updateUploadFileReq;
    private String title;
    private String description;
    private List<CreateProjectInfoReq> createdProjectInfoList = new ArrayList<>();
    private List<UpdateProjectInfoReq> updatedProjectInfoList = new ArrayList<>();
    private List<RemoveProjectInfoReq> removedProjectInfoList = new ArrayList<>();
}
