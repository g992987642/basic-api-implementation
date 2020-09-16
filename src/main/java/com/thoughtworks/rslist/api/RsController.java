package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.UserList;
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
    public RsEvent getRsEvent(@PathVariable int index) {
        return rsList.get(index - 1);
    }

    @GetMapping("/rs/event")
    public List<RsEvent> getRsEventByRange(@RequestParam int start, @RequestParam int end) {
        return rsList.subList(start - 1, end);
    }

    @GetMapping("/rs/list")
    public List<RsEvent> getAllRsEvent() {
        return rsList;
    }

    @PostMapping("/rs/event")
    public void addOneRsEvent(@RequestBody @Valid RsEvent rsEvent) throws JsonProcessingException {
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
    }

    @PutMapping("/rs/event")
    public void modifyOneRsEvent(@RequestParam int index,@RequestParam(required = false) String eventName,@RequestParam String keyWord) throws JsonProcessingException {
        RsEvent rsEvent = rsList.get(index-1);
        if(eventName!=null){
            rsEvent.setEventName(eventName);
        }
        if(keyWord!=null){
            rsEvent.setKeyWord(keyWord);
        }
    }

    @DeleteMapping("/rs/event/{index}")
    public void deleteOneRsEvent(@PathVariable int index) throws JsonProcessingException {
       rsList.remove(index-1);
    }

}