package com.thoughtworks.rslist.entity;


import com.thoughtworks.rslist.dto.RsEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user")
public class UserEntity {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Integer id;
    @Column(name ="name")
    private String userName;
    private  String gender;
    private int age;
    private  String email;
    private String phone;
    private int voteNum;
    @OneToMany(mappedBy = "userEntity", cascade= CascadeType.REMOVE)
    private List<RsEventEntity> rsEvents;

}
