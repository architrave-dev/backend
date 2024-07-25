package com.architrave.portfolio.domain.model.builder;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.UploadFile;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProjectBuilder {

    private Member member;
    private String title;
    private String description;
    private String originImgUrl;
    private String thumbnailUrl;

    public ProjectBuilder member(Member member) {
        this.member = member;
        return this;
    }
    public ProjectBuilder title(String title) {
        this.title = title;
        return this;
    }
    public ProjectBuilder description(String description) {
        this.description = description;
        return this;
    }
    public ProjectBuilder originImgUrl(String originImgUrl){
        this.originImgUrl = originImgUrl;
        return this;
    }
    public ProjectBuilder thumbnailUrl(String thumbnailUrl){
        this.thumbnailUrl = thumbnailUrl;
        return this;
    }
    /**
     * originImgUrl과 thumbnailUrl는 필수값 입니다. <br/>
     * originImgUrl과 thumbnailUrl로 UploadFile을 생성합니다.
     * @return Project
     */
    public Project build(){
        validateProject();

        UploadFile uploadFile = UploadFile.builder()
                .originUrl(originImgUrl)
                .thumbnailUrl(thumbnailUrl)
                .build();

        return Project.createProject(
                member,
                uploadFile,
                title,
                description
        );
    }
    private void validateProject(){
        if(member == null || title == null || originImgUrl == null || thumbnailUrl == null){
            throw new IllegalArgumentException("required value is empty in ProjectBuilder");
        }
    }
}
