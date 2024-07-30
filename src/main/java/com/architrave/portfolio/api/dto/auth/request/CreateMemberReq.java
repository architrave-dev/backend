package com.architrave.portfolio.api.dto.auth.request;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMemberReq{
    @Schema(example = "example@gmail.com")
    @NotEmpty
    @Email
    private String email;

    @Schema(example = "string(아직 요구되는 길이,특수문자 없음)")
    @NotEmpty
    private String password;

    @Schema(example = "string(중복가능)")
    @NotEmpty
    private String username;
}