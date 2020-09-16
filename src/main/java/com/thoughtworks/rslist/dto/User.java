package com.thoughtworks.rslist.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class User {
    @NotEmpty
    @Size(max = 8)
    String userName;
    @NotNull
    @Max(100)
    @Min(18)
    int age;
    @NotEmpty
    String gender;
    @Email
    String email;
    @Pattern(regexp = "^1(\\d){10}$")
    String phone;

}
