package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import org.springframework.web.bind.annotation.*;

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
    public void addOneRsEvent(@RequestBody String rsEventStr) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        RsEvent rsEvent = objectMapper.readValue(rsEventStr, RsEvent.class);
        rsList.add(rsEvent);
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
