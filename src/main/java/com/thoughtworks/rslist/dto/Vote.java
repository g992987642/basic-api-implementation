package com.thoughtworks.rslist.dto;


import lombok.*;

import javax.validation.constraints.NotNull;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Vote {

    @NotNull
     int voteNum;
    Timestamp voteTime;
    @NotNull
    int userId;
    @NotNull
    private int rsEventId;
}
