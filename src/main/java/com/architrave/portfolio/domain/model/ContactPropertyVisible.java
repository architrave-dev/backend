package com.architrave.portfolio.domain.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Entity
@Data
@EqualsAndHashCode(callSuper=false)
public class ContactPropertyVisible {
    @Id
    @GeneratedValue
    @Column(name = "contact_property_visible_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    private Boolean address;
    private Boolean email;
    private Boolean contact;
    private Boolean twitter;
    private Boolean instagram;
    private Boolean facebook;
    private Boolean youtube;
    private Boolean url1;

    protected ContactPropertyVisible(){}
    public ContactPropertyVisible(Member member){
        this.member = member;
        this.address = true;
        this.email = true;
        this.contact = true;
        this.twitter = true;
        this.instagram = true;
        this.facebook = true;
        this.youtube = true;
        this.url1 = true;
    }
}