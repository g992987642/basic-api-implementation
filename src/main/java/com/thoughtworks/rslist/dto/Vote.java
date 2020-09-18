package com.thoughtworks.rslist.dto;


import lombok.*;

import javax.validation.constraints.NotNull;
import java.sql.Date;


@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Vote {

    @NotNull
     int voteNum;
    Date voteTime;
    @NotNull
    int userId;
    @NotNull
    private int rsEventId;
}
