package com.thoughtworks.rslist.utils;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.response.RsEventResponse;

import java.util.List;
import java.util.stream.Collectors;

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

    public static UserDto convertUserEntityToDto(UserEntity userEntity){

        UserDto userDto=UserDto.builder()
                .userName(userEntity.getUserName())
                .age(userEntity.getAge())
                .email(userEntity.getEmail())
                .gender(userEntity.getGender())
                .phone(userEntity.getPhone())
                .voteNum(userEntity.getVoteNum())
                .build();
        return userDto;
    }

    public static  List<UserDto> convertUserEntityListToDtoList(List<UserEntity> userEntityList){

        List<UserDto> userDtoyList = userEntityList.stream()
                .map(CommonUtils::convertUserEntityToDto)
                .collect(Collectors.toList());
        return userDtoyList;
    }






    public static RsEventEntity convertRsDtoToEntity(RsEvent rsEvent){
        RsEventEntity rsEventEntity= RsEventEntity.builder()
                .eventName(rsEvent.getEventName())
                .keyword(rsEvent.getKeyWord())
                .voteNum(rsEvent.getVotesNum())
                .build();

        return rsEventEntity;
    }

    public static RsEvent convertRsEntityToDto(RsEventEntity rsEventEntity){
        RsEvent rsEvent= RsEvent.builder()
                .eventName(rsEventEntity.getEventName())
                .keyWord(rsEventEntity.getKeyword())
                .votesNum(rsEventEntity.getVoteNum())
                .build();
        return rsEvent;
    }



    public static  List<RsEvent> convertRsEntityListToDtoList(List<RsEventEntity> rsEventEntityList){

        List<RsEvent> rsEventList = rsEventEntityList.stream()
                .map(CommonUtils::convertRsEntityToDto)
                .collect(Collectors.toList());
        return rsEventList;
    }


    public static RsEventResponse convertRsEntityToResponse(RsEventEntity rsEventEntity){
        RsEventResponse rsEventResponse= RsEventResponse.builder()
                .id(rsEventEntity.getId())
                .eventName(rsEventEntity.getEventName())
                .keyword(rsEventEntity.getKeyword())
                .voteNum(rsEventEntity.getVoteNum())
                .build();
        return rsEventResponse;
    }



    public static  List<RsEventResponse> convertRsEntityListToResponseList(List<RsEventEntity> rsEventEntityList){
        List<RsEventResponse> rsEventResponseList = rsEventEntityList.stream()
                .map(CommonUtils::convertRsEntityToResponse)
                .collect(Collectors.toList());
        return rsEventResponseList;
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
