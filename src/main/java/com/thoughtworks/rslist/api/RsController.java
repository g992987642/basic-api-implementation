package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.UserList;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exception.CommentError;
import com.thoughtworks.rslist.exception.InvalidIndexException;
import com.thoughtworks.rslist.exception.InvalidRequestParamException;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.utils.CommonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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

    public RsController(UserRepository userRepository, RsEventRepository rsEventRepository) {
        this.userRepository = userRepository;
        this.rsEventRepository = rsEventRepository;
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

        return  ResponseEntity.ok().body(rsList);
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

    @DeleteMapping("/rs/event/{index}")
    public ResponseEntity deleteOneRsEvent(@PathVariable int index) throws JsonProcessingException {
        rsList.remove(index - 1);
        return ResponseEntity.ok().build();
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleException(MethodArgumentNotValidException ex) {
        CommentError commentError = new CommentError("invalid param");
        logger.error("[LOGGING]:" + commentError.getError());
        return ResponseEntity.badRequest().body(commentError);
    }

}