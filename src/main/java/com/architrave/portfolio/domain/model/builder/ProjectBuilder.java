package com.architrave.portfolio.domain.model.builder;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Project;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProjectBuilder {

    private Member member;
    private String title;
    private String description;

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

    public Project build(){
        validateProject();
        return Project.createProject(
                member,
                title,
                description
        );
    }
    private void validateProject(){
        if(member == null || title == null){
            throw new IllegalArgumentException("required value is empty in ProjectBuilder");
        }
    }
}
