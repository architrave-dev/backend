package com.architrave.portfolio.domain.model;

import com.architrave.portfolio.domain.model.enumType.CareerType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class Career extends BaseEntity{

    @Id @GeneratedValue
    @Column(name="career_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private CareerType careerType;

    private Integer yearFrom;
    private Integer yearTo;
    private String content;


    public static Career createCareer(Member member, CareerType careerType, Integer yearFrom, Integer yearTo, String content) {
        Career career = new Career();
        career.member = member;
        career.careerType = careerType;
        career.content = content;
        career.yearFrom = yearFrom;
        career.yearTo = yearTo;
        return career;
    }
}
