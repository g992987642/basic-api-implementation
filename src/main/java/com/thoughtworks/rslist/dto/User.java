package com.thoughtworks.rslist.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    String userName;
    int age;
    String gender;
    String email;
    String phone;

}
