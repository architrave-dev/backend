package com.architrave.portfolio.api.dto.contact.request;

import com.architrave.portfolio.domain.model.MemberInfo;
import com.architrave.portfolio.domain.model.SocialMedia;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UpdateContactReq {
    @NotNull
    private Long id;
    private String address;
    private String email;
    private String contact;
    private SocialMedia sns;
}
