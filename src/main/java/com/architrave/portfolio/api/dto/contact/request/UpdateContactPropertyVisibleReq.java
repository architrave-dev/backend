package com.architrave.portfolio.api.dto.contact.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateContactPropertyVisibleReq {
    @NotNull
    private Long contactPropertyVisibleId;
    private Boolean address;
    private Boolean email;
    private Boolean contact;
    private Boolean twitter;
    private Boolean instagram;
    private Boolean facebook;
    private Boolean youtube;
    private Boolean url1;
}
