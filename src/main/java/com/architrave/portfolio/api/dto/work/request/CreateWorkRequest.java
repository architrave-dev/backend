package com.architrave.portfolio.api.dto.work.request;

import com.architrave.portfolio.domain.model.Member;
import com.architrave.portfolio.domain.model.Size;
import com.architrave.portfolio.domain.model.Work;
import lombok.Data;

@Data
public class CreateWorkRequest {
    private String title;
    private String description;
    private Size size;
    private String material;
    private Integer prodYear;

    public Work toWork(Member member) {
        return Work.builder()
                .member(member)
                .title(this.title)
                .description(this.description)
                .size(this.size)
                .material(this.material)
                .prodYear(this.prodYear)
                .build();
    }
}
