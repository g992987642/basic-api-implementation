package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VotesRepository;
import com.thoughtworks.rslist.response.RsEventResponse;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;


public interface RsService {

    public RsEventResponse getRsEvent(int index);

    public List<RsEventResponse> getRsEventByRange(int start, int end);

    public List<RsEventResponse> getAllRsEvent();

    public Boolean addOneRsEvent(RsEvent rsEvent);

    public void modifyOneRsEventByIndex(int index, String eventName, String keyWord);

    public void deleteEventByIndex(int index);

    public void deleteEventByUserId(int id);

    public Boolean modifyEventById(int rsEventId, RsEvent rsEvent);

    public Boolean voteForRsEventById(Integer rsEventId, Vote vote);

}
