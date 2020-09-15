package com.thoughtworks.rslist.api;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
    private List<String> rsList = initRsList();

    private List<String> initRsList() {
        List<String> tempList = new ArrayList<>();
        tempList.add("第一条事件");
        tempList.add("第二条事件");
        tempList.add("第三条事件");
        return tempList;
    }


    @GetMapping("/rs/{index}")
    public String getRsEvent(@PathVariable int index) {
        return rsList.get(index - 1);
    }

    @GetMapping("/rs/event")
    public String getRsEventByRange(@RequestParam int start, @RequestParam int end) {
        return rsList.subList(start - 1, end).toString();
    }

    @GetMapping("/rs/list")
    public String getAllRsEvent() {
        return rsList.toString();
    }

    @PostMapping("/rs/event")
    public void addOneRsEvent(@RequestBody String event) {
        rsList.add(event);
    }


}
