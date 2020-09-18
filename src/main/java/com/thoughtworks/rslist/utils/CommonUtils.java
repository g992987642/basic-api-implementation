package com.thoughtworks.rslist.utils;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;

public  class CommonUtils {

    public static UserEntity convertUserDtoToEntity(UserDto userDto){

        UserEntity userEntity=UserEntity.builder()
                .userName(userDto.getUserName())
                .age(userDto.getAge())
                .email(userDto.getEmail())
                .gender(userDto.getGender())
                .phone(userDto.getPhone())
                .voteNum(userDto.getVoteNum())
                .build();
        return userEntity;
    }

    public static RsEventEntity converRsDtoToEntity(RsEvent rsEvent){
        RsEventEntity rsEventEntity= RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyWord())
                .voteNum(rsEvent.getVotesNum())
                .build();

        return rsEventEntity;
    }

    public static VoteEntity converVotesToEntity(Vote votes, int rsEventId){
        VoteEntity votesEntity= VoteEntity.builder()
                .userId(votes.getUserId())
                .voteNum(votes.getVoteNum())
                .voteTime(votes.getVoteTime())
                .rsEventId(rsEventId)
                .build();
        return votesEntity;
    }




}
