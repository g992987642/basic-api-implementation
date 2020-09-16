package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.corba.se.impl.orbutil.StackImpl;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
public class RsController {
    private List<RsEvent> rsList = initRsList();

       static  List<User> userList=new ArrayList<>();
    static{
        userList.add(new User("guhao",18,"男","12345678@qq.com","12345678910"));
    }

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
        User user = rsEvent.getUser();
        if(!userList.contains(user)){
            userList.add(user);
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