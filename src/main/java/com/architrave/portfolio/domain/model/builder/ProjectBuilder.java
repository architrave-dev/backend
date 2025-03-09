package com.architrave.portfolio.domain.model.builder;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Project;
import com.architrave.portfolio.domain.model.UploadFile;
import com.architrave.portfolio.global.exception.custom.RequiredValueEmptyException;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProjectBuilder {

    private Member member;
    private String title;
    private String description;
    private String originUrl;
    private Integer index;

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
    public ProjectBuilder originUrl(String originUrl){
        this.originUrl = originUrl;
        return this;
    }
    public ProjectBuilder index(Integer index){
        this.index = index;
        return this;
    }
    /**
     * originUrl=는 필수값 입니다. <br/>
     * originUrl로 UploadFile을 생성합니다.
     * @return Project
     */
    public Project build(){
        validateProject();

        UploadFile uploadFile = UploadFile.builder()
                .originUrl(originUrl)
                .build();

        return Project.createProject(
                member,
                uploadFile,
                title,
                description,
                index
        );
    }
    private void validateProject(){
        if(member == null || title == null || originUrl == null || index == null ){
            throw new RequiredValueEmptyException("required value is empty in ProjectBuilder");
        }
    }
}
