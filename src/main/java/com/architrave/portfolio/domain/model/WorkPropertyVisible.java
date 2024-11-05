package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class WorkPropertyVisible {
    @Id
    @GeneratedValue
    @Column(name = "work_property_visible_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Boolean workType;
    private Boolean imageUrl;
    private Boolean description;
    private Boolean price;
    private Boolean collection;

    protected WorkPropertyVisible(){}
    public WorkPropertyVisible(Member member){
        this.member = member;
        this.workType = true;
        this.imageUrl = true;
        this.description = true;
        this.price = true;
        this.collection = true;

    }
}