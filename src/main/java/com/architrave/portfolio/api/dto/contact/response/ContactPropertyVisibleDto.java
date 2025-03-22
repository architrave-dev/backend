package com.architrave.portfolio.api.dto.contact.response;

import com.architrave.portfolio.domain.model.ContactPropertyVisible;
import lombok.Data;

@Data
public class ContactPropertyVisibleDto {
    private Long contactPropertyVisibleId;
    private Boolean address;
    private Boolean email;
    private Boolean contact;
    private Boolean twitter;
    private Boolean instagram;
    private Boolean facebook;
    private Boolean youtube;
    private Boolean url1;

    public ContactPropertyVisibleDto(ContactPropertyVisible contactPropertyVisible){
        this.contactPropertyVisibleId = contactPropertyVisible.getId();
        this.address = contactPropertyVisible.getAddress();
        this.email = contactPropertyVisible.getEmail();
        this.contact = contactPropertyVisible.getContact();
        this.twitter = contactPropertyVisible.getTwitter();
        this.instagram = contactPropertyVisible.getInstagram();
        this.facebook = contactPropertyVisible.getFacebook();
        this.youtube = contactPropertyVisible.getYoutube();
        this.url1 = contactPropertyVisible.getUrl1();
    }
}
