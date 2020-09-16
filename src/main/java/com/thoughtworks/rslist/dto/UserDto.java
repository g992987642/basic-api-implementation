package com.thoughtworks.rslist.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserDto {
    @NotEmpty
    @Size(max = 8)
    @JsonProperty("user_name")
    String userName;
    @NotNull
    @Max(100)
    @Min(18)
    @JsonProperty("user_age")
    int age;
    @NotEmpty
    @JsonProperty("user_gender")
    String gender;
    @Email
    @JsonProperty("user_email")
    String email;
    @Pattern(regexp = "^1(\\d){10}$")
    @JsonProperty("user_phone")
    String phone;

}
