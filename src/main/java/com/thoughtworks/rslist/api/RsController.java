package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.exception.CommentError;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.exception.InvalidRequestParamException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VotesRepository;
import com.thoughtworks.rslist.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class RsController {
    Logger logger = LoggerFactory.getLogger(RsController.class);
    private List<RsEvent> rsList = initRsList();

    UserRepository userRepository;
    RsEventRepository rsEventRepository;
    VotesRepository votesRepository;

    public RsController(UserRepository userRepository, RsEventRepository rsEventRepository, VotesRepository votesRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
        this.votesRepository = votesRepository;
    }

    private List<RsEvent> initRsList() {
        List<RsEvent> tempList = new ArrayList<>();
        tempList.add(new RsEvent("第一条事件", "无分类"));
        tempList.add(new RsEvent("第二条事件", "无分类"));
        tempList.add(new RsEvent("第三条事件", "无分类"));

        return tempList;
    }


    @GetMapping("/rs/{index}")
    @JsonView(RsEvent.RsViewWithoutUser.class)
    public ResponseEntity getRsEvent(@PathVariable int index) {
        if(index<=0||index>rsList.size()){
            throw  new InvalidIndexException();
        }
        RsEvent rsEvent = rsList.get(index - 1);
        return ResponseEntity.ok().body(rsEvent);
    }

    @GetMapping("/rs/event")
    @JsonView(RsEvent.RsViewWithoutUser.class)
    public ResponseEntity  getRsEventByRange(@RequestParam int start, @RequestParam int end) {
        if(start<=0||start>end||end>rsList.size()){
            throw new InvalidRequestParamException();
        }
        List<RsEvent> rsEvents = rsList.subList(start - 1, end);
        return  ResponseEntity.ok().body(rsEvents);
    }

    @GetMapping("/rs/list")
    @JsonView(RsEvent.RsViewWithoutUser.class)
    public ResponseEntity getAllRsEvent() {
        List<RsEventEntity> all = rsEventRepository.findAll();

        //TODO，把Entity改成DTO再传
        return  ResponseEntity.ok().body(all);
    }



    @PostMapping("/rs/event")
    public ResponseEntity addOneRsEvent(@RequestBody @Valid RsEvent rsEvent) throws JsonProcessingException {

        Optional<UserEntity> user = userRepository.findById(rsEvent.getUserId());

        if(!user.isPresent()){
            return ResponseEntity.badRequest().build();
        }
        RsEventEntity rsEventEntity = CommonUtils.converRsDtoToEntity(rsEvent);
        rsEventRepository.save(rsEventEntity);

        return ResponseEntity.status(201).build();
    }

    @PutMapping("/rs/event")
    public ResponseEntity modifyOneRsEvent(@RequestParam int index, @RequestParam(required = false) String eventName, @RequestParam String keyWord) throws JsonProcessingException {
        RsEvent rsEvent = rsList.get(index - 1);
        if (eventName != null) {
            rsEvent.setEventName(eventName);
        }
        if (keyWord != null) {
            rsEvent.setKeyWord(keyWord);
        }
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/rs/event/{id}")
    @Transactional
    public ResponseEntity deleteEventById(@PathVariable int id)  {
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/rs/{rsEventId}")
    public ResponseEntity modifyEventById(@PathVariable int rsEventId,@RequestBody @Valid RsEvent rsEvent)  {
        Optional<RsEventEntity> rsEventEntityOptional = rsEventRepository.findById(rsEventId);
        if(!rsEventEntityOptional.isPresent()||rsEvent.getUserId()!=rsEventEntityOptional.get().getUserEntity().getId()){
            ResponseEntity.badRequest().build();
        }
        RsEventEntity rsEventEntity = rsEventEntityOptional.get();
        if(rsEvent.getEventName()!=null){
            rsEventEntity.setEventName(rsEvent.getEventName());
        }
        if(rsEvent.getKeyWord()!=null){
            rsEventEntity.setEventName(rsEvent.getKeyWord());
        }
        rsEventRepository.save(rsEventEntity);
        return ResponseEntity.ok().build();
    }



    @PostMapping("/rs/vote/{rsEventId}")
    @Transactional
    public ResponseEntity voteForRsEventById(@PathVariable Integer rsEventId, @RequestBody @Valid Vote vote)  {
        Optional<UserEntity> userEntityOptional = userRepository.findById(vote.getUserId());
        Optional<RsEventEntity> rsEventEntityOptional = rsEventRepository.findById(rsEventId);
        int prepareToVoteNum=vote.getVoteNum();
        if(!userEntityOptional.isPresent()
                ||!rsEventEntityOptional.isPresent()
                ||prepareToVoteNum<0
                ||prepareToVoteNum>userEntityOptional.get().getVoteNum()){
            return  ResponseEntity.badRequest().build();
        }
        UserEntity userEntity = userEntityOptional.get();
        userEntity.setVoteNum(userEntity.getVoteNum()-prepareToVoteNum);

        RsEventEntity rsEventEntity=rsEventEntityOptional.get();
        rsEventEntity.setVoteNum(rsEventEntity.getVoteNum()+prepareToVoteNum);

        VoteEntity votesEntity = CommonUtils.converVotesToEntity(vote,rsEventId);

        userRepository.save(userEntity);
        rsEventRepository.save(rsEventEntity);
        votesRepository.save(votesEntity);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleException(MethodArgumentNotValidException ex) {
        CommentError commentError = new CommentError("invalid param");
        logger.error("[LOGGING]:" + commentError.getError());
        return ResponseEntity.badRequest().body(commentError);
    }

}