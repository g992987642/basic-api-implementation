package com.thoughtworks.rslist.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@Builder
public class UserDto {
    @NotEmpty
    @Size(max = 8)
    @JsonProperty("user_name")
    private String userName;
    @NotNull
    @Max(100)
    @Min(18)
    @JsonProperty("user_age")
    private int age;
    @NotEmpty
    @JsonProperty("user_gender")
    private String gender;
    @Email
    @JsonProperty("user_email")
    private String email;
    @Pattern(regexp = "^1(\\d){10}$")
    @JsonProperty("user_phone")
    private String phone;
    private int voteNum;

    public UserDto(@NotEmpty @Size(max = 8) String userName, @NotNull @Max(100) @Min(18) int age, @NotEmpty String gender, @Email String email, @Pattern(regexp = "^1(\\d){10}$") String phone) {
        this.userName = userName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
    }
}
