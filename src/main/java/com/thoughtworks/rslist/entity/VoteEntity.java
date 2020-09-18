package com.thoughtworks.rslist.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="vote")
public class VoteEntity {
    @Id
    @GeneratedValue
    @Column(name = "votes_id")
    Integer id;
    int voteNum;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    Timestamp voteTime;
    int userId;
    int rsEventId;

}
