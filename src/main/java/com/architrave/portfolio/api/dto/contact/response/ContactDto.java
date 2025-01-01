package com.architrave.portfolio.api.dto.contact.response;

import com.architrave.portfolio.domain.model.MemberInfo;
import com.architrave.portfolio.domain.model.SocialMedia;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ContactDto {

    private Long id;
    private String address;
    private String email;
    private String contact;
    private SocialMedia sns;

    public ContactDto(MemberInfo memberInfo) {
        this.id = memberInfo.getId();
        this.email = memberInfo.getEmail();
        this.contact = memberInfo.getContact();
        this.address = memberInfo.getAddress();
        this.sns = memberInfo.getSns();
    }
}
