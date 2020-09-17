package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.RsEventWithoutUser;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.UserList;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
    private List<RsEvent> rsList = initRsList();


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
        RsEvent rsEvent = rsList.get(index - 1);
        return ResponseEntity.ok().body(rsEvent);
    }

    @GetMapping("/rs/event")
    @JsonView(RsEvent.RsViewWithoutUser.class)
    public ResponseEntity  getRsEventByRange(@RequestParam int start, @RequestParam int end) {
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
        rsList.add(rsEvent);
        UserDto userDto = rsEvent.getUserDto();
        if(userDto!=null){
            boolean isRegisterd=false;
            for(UserDto registerUserDto : UserList.userList){
                if(registerUserDto.getUserName().equals(userDto.getUserName())){
                    isRegisterd=true;
                }
            }
            if(!isRegisterd){
                UserList.userList.add(userDto);
            }
        }
        return  ResponseEntity.status(201).header("index",String.valueOf(rsList.size()-1)).build();
    }

    @PutMapping("/rs/event")
    public ResponseEntity modifyOneRsEvent(@RequestParam int index,@RequestParam(required = false) String eventName,@RequestParam String keyWord) throws JsonProcessingException {
        RsEvent rsEvent = rsList.get(index-1);
        if(eventName!=null){
            rsEvent.setEventName(eventName);
        }
        if(keyWord!=null){
            rsEvent.setKeyWord(keyWord);
        }
        return  ResponseEntity.ok().build();
    }

    @DeleteMapping("/rs/event/{index}")
    public ResponseEntity deleteOneRsEvent(@PathVariable int index) throws JsonProcessingException {
       rsList.remove(index-1);
        return  ResponseEntity.ok().build();
    }


    private List<RsEventWithoutUser> getRsEventWithoutUsers(List<RsEvent> rsList) {
        List<RsEventWithoutUser> rsEventWithoutUsers = new ArrayList<>();
        for (RsEvent rsEvent : rsList) {
            RsEventWithoutUser rsEventWithoutUser = new RsEventWithoutUser(rsEvent.getEventName(), rsEvent.getKeyWord());
            rsEventWithoutUsers.add(rsEventWithoutUser);
        }
        return rsEventWithoutUsers;
    }
}