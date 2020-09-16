package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsControllerTest {

    @Autowired
    MockMvc mockMVC;

    @Test
    void should_get_one_rs_event() throws Exception {
        mockMVC.perform(get("/rs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName",is("第一条事件")))
                .andExpect(jsonPath("$.keyWord",is("无分类")));
        mockMVC.perform(get("/rs/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName",is("第二条事件")))
                .andExpect(jsonPath("$.keyWord",is("无分类")));
        mockMVC.perform(get("/rs/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName",is("第三条事件")))
                .andExpect(jsonPath("$.keyWord",is("无分类")));

    }

    @Test
    void should_get_rs_event_by_range() throws Exception {
        mockMVC.perform(get("/rs/event?start=1&end=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无分类")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无分类")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("无分类")));

    }

    @Test
    void should_get_all_event() throws Exception {
        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无分类")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无分类")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("无分类")));
    }

    @Test
    void should_add_one_rs_event() throws Exception {
        RsEvent rsEvent=new RsEvent("猪肉涨价了","经济");
        ObjectMapper objectMapper=new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)));
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(4)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无分类")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无分类")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("无分类")))
                .andExpect(jsonPath("$[3].eventName",is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyWord",is("经济")));

    }

    @Test
    void should_modify_one_event() throws Exception {
        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无分类")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无分类")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("无分类")));

        mockMVC.perform(put("/rs/event?index=1&keyWord=哈哈"))
                .andExpect(status().isOk());

        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("哈哈")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无分类")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("无分类")));

    }

    @Test
    void should_delete_one_event() throws Exception {
        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无分类")))
                .andExpect(jsonPath("$[1].eventName",is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无分类")))
                .andExpect(jsonPath("$[2].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord",is("无分类")));

        mockMVC.perform(delete("/rs/event/2"))
                .andExpect(status().isOk());

        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(2)))
                .andExpect(jsonPath("$[0].eventName",is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord",is("无分类")))
                .andExpect(jsonPath("$[1].eventName",is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord",is("无分类")));


    }


    @Test
    void should_not_add_one_rs_when_userName_is_null() throws Exception {

       User user= new User(null,18,"男","12345678@qq.com","12345678910");
        RsEvent rsEvent=new RsEvent("猪肉涨价了","经济",user);
        ObjectMapper objectMapper=new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)));
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void should_not_add_one_rs_when_size_of_userName_over_8() throws Exception {

        User user= new User("123456789",18,"男","12345678@qq.com","12345678910");
        RsEvent rsEvent=new RsEvent("猪肉涨价了","经济",user);
        ObjectMapper objectMapper=new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)));
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_user_age_less_than_18() throws Exception {

        User user= new User("12345678",17,"男","12345678@qq.com","12345678910");
        RsEvent rsEvent=new RsEvent("猪肉涨价了","经济",user);
        ObjectMapper objectMapper=new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)));
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_user_age_more_than_100() throws Exception {

        User user= new User("12345678",101,"男","12345678@qq.com","12345678910");
        RsEvent rsEvent=new RsEvent("猪肉涨价了","经济",user);
        ObjectMapper objectMapper=new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)));
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_user_gender_is_null() throws Exception {

        User user= new User("12345678",100,null,"12345678@qq.com","12345678910");
        RsEvent rsEvent=new RsEvent("猪肉涨价了","经济",user);
        ObjectMapper objectMapper=new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)));
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_user_email_is_not_conform_shares() throws Exception {

        User user= new User("12345678",100,"男","12345678qq.com","12345678910");
        RsEvent rsEvent=new RsEvent("猪肉涨价了","经济",user);
        ObjectMapper objectMapper=new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(3)));
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }



}