package com.thoughtworks.rslist.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RsEventResponse {
    int id;
    String eventName;
    String keyword;
    int voteNum;
}
