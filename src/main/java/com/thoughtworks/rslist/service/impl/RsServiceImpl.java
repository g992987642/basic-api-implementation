package com.thoughtworks.rslist.service.impl;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.exception.InvalidRequestParamException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VotesRepository;
import com.thoughtworks.rslist.response.RsEventResponse;
import com.thoughtworks.rslist.service.RsService;
import com.thoughtworks.rslist.utils.CommonUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;


@AllArgsConstructor
public class RsServiceImpl implements RsService {


    private final UserRepository userRepository;

    private final RsEventRepository rsEventRepository;

    private final VotesRepository votesRepository;

    @Override
    public RsEventResponse getRsEvent(int index) {
        List<RsEventEntity> allRsEventEntity = rsEventRepository.findAll();
        if (index <= 0 || index > allRsEventEntity.size()) {
            throw new InvalidIndexException();
        }
        RsEventEntity rsEventEntity = allRsEventEntity.get(index - 1);
        RsEventResponse rsEventResponse = CommonUtils.convertRsEntityToResponse(rsEventEntity);
        return rsEventResponse;
    }


    @Override
    public List<RsEventResponse> getRsEventByRange(int start, int end) {
        List<RsEventEntity> allRsEventEntity = rsEventRepository.findAll();
        if (start <= 0 || start > end || end > allRsEventEntity.size()) {
            throw new InvalidRequestParamException();
        }
        List<RsEventEntity> rsEventsEventList = allRsEventEntity.subList(start - 1, end);
        List<RsEventResponse> rsEventResponseList = CommonUtils.convertRsEntityListToResponseList(rsEventsEventList);
        return rsEventResponseList;
    }

    @Override
    public List<RsEventResponse> getAllRsEvent() {
        List<RsEventEntity> RsEventEntityList = rsEventRepository.findAll();
        List<RsEventResponse> rsEventResponseList = CommonUtils.convertRsEntityListToResponseList(RsEventEntityList);
        return rsEventResponseList;
    }

    @Override
    public Boolean addOneRsEvent(RsEvent rsEvent) {
        Optional<UserEntity> user = userRepository.findById(rsEvent.getUserId());
        if (!user.isPresent()) {
            return false;
        }
        RsEventEntity rsEventEntity = CommonUtils.convertRsDtoToEntity(rsEvent);
        rsEventRepository.save(rsEventEntity);
        return true;
    }

    @Override
    public void modifyOneRsEventByIndex(int index, String eventName, String keyWord) {
        List<RsEventEntity> RsEventEntityList = rsEventRepository.findAll();
        RsEventEntity rsEventEntity = RsEventEntityList.get(index - 1);
        if (eventName != null) {
            rsEventEntity.setEventName(eventName);
        }
        if (keyWord != null) {
            rsEventEntity.setKeyword(keyWord);
        }
        rsEventRepository.save(rsEventEntity);
    }

    @Override
    public void deleteEventByIndex(int index) {
        List<RsEventEntity> rsEventEntityList = rsEventRepository.findAll();
        Integer rsEventEntityId = rsEventEntityList.get(index - 1).getId();
        rsEventRepository.deleteById(rsEventEntityId);
    }

    @Override
    public void deleteEventById(int id) {
        userRepository.deleteById(id);
    }


    @Override
    public Boolean modifyEventById(int rsEventId, RsEvent rsEvent) {
        Optional<RsEventEntity> rsEventEntityOptional = rsEventRepository.findById(rsEventId);
        if (!rsEventEntityOptional.isPresent() || rsEvent.getUserId() != rsEventEntityOptional.get().getUserEntity().getId()) {
            return false;
        }
        RsEventEntity rsEventEntity = rsEventEntityOptional.get();
        if (rsEvent.getEventName() != null) {
            rsEventEntity.setEventName(rsEvent.getEventName());
        }
        if (rsEvent.getKeyWord() != null) {
            rsEventEntity.setEventName(rsEvent.getKeyWord());
        }
        rsEventRepository.save(rsEventEntity);
        return true;
    }

    @Override
    public Boolean voteForRsEventById(Integer rsEventId, Vote vote) {
        Optional<UserEntity> userEntityOptional = userRepository.findById(vote.getUserId());
        Optional<RsEventEntity> rsEventEntityOptional = rsEventRepository.findById(rsEventId);
        int prepareToVoteNum = vote.getVoteNum();
        if (!userEntityOptional.isPresent()
                || !rsEventEntityOptional.isPresent()
                || prepareToVoteNum < 0
                || prepareToVoteNum > userEntityOptional.get().getVoteNum()) {
            return false;
        }
        UserEntity userEntity = userEntityOptional.get();
        userEntity.setVoteNum(userEntity.getVoteNum() - prepareToVoteNum);

        RsEventEntity rsEventEntity = rsEventEntityOptional.get();
        rsEventEntity.setVoteNum(rsEventEntity.getVoteNum() + prepareToVoteNum);

        VoteEntity votesEntity = CommonUtils.converVotesToEntity(vote, rsEventId);

        userRepository.save(userEntity);
        rsEventRepository.save(rsEventEntity);
        votesRepository.save(votesEntity);
        return true;
    }
}
