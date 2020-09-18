package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "rs_event")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data

public class RsEventEntity {
    @Id
    @GeneratedValue
    @Column(name="event_id")
    private Integer id;
    @Column(name = "name")
    private String eventName;
    private String keyword;
    private int voteNum;
    @ManyToOne
    @JoinColumn(name = "user_id" )
    private UserEntity userEntity;
}
