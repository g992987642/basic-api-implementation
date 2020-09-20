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
import com.thoughtworks.rslist.response.RsEventResponse;
import com.thoughtworks.rslist.service.RsService;
import com.thoughtworks.rslist.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RsController {
    Logger logger = LoggerFactory.getLogger(RsController.class);

    private final RsService rsService;

    // @GetMapping("/rs/{index}")
    @GetMapping("/rsEvent/{index}")
    public ResponseEntity getRsEvent(@PathVariable int index) {
        RsEventResponse rsEventResponse = rsService.getRsEvent(index);
        return ResponseEntity.ok().body(rsEventResponse);
    }

    // @GetMapping("/rs/event")
    @GetMapping("/rsEvent")
    public ResponseEntity getRsEventByRange(@RequestParam int start, @RequestParam int end) {
        List<RsEventResponse> rsEventResponseList = rsService.getRsEventByRange(start, end);
        return ResponseEntity.ok().body(rsEventResponseList);
    }

    //    @GetMapping("/rs/list")
    @GetMapping("/rsEvents")
    public ResponseEntity getAllRsEvent() {
        List<RsEventResponse> rsEventResponseList = rsService.getAllRsEvent();
        return ResponseEntity.ok().body(rsEventResponseList);
    }


    // @PostMapping("/rs/event")
    @PostMapping("/rsEvent")
    public ResponseEntity addOneRsEvent(@RequestBody @Valid RsEvent rsEvent) throws JsonProcessingException {
        Boolean isSuccess = rsService.addOneRsEvent(rsEvent);
        if (!isSuccess) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(201).build();
    }

    //@PutMapping("/rs/event")
    @PutMapping("/rsEvent")
    public ResponseEntity modifyOneRsEventByIndex(@RequestParam int index, @RequestParam(required = false) String eventName, @RequestParam String keyWord) {
        rsService.modifyOneRsEventByIndex(index, eventName, keyWord);
        return ResponseEntity.ok().build();
    }

    //@DeleteMapping("/rs/event/index/{index}")
    @DeleteMapping("/rsEvent/index/{index}")
    @Transactional
    public ResponseEntity deleteEventByIndex(@PathVariable int index) {
        rsService.deleteEventByIndex(index);
        return ResponseEntity.ok().build();
    }

    //@DeleteMapping("/rs/event/userId/{id}")
    @DeleteMapping("/rsEvent/userId/{id}")
    @Transactional
    public ResponseEntity deleteEventByUserId(@PathVariable int id) {
        rsService.deleteEventByUserId(id);
        return ResponseEntity.ok().build();
    }


    //@PatchMapping("/rs/{rsEventId}")
    @PatchMapping("/rsEvent/{rsEventId}")
    @Transactional
    public ResponseEntity modifyEventById(@PathVariable int rsEventId, @RequestBody @Valid RsEvent rsEvent) {
        Boolean isSuccess = rsService.modifyEventById(rsEventId, rsEvent);
        if (!isSuccess) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }


    @PostMapping("/rsEvent/vote/{rsEventId}")
    @Transactional
    public ResponseEntity voteForRsEventById(@PathVariable Integer rsEventId, @RequestBody @Valid Vote vote) {
        Boolean isSuccess = rsService.voteForRsEventById(rsEventId, vote);
        if (!isSuccess) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, AssertionError.class})
    public ResponseEntity handleException(Exception ex) {
        CommentError commentError = new CommentError("invalid param");
        logger.error("[LOGGING]:" + commentError.getError());
        return ResponseEntity.badRequest().body(commentError);
    }

}