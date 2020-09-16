package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.UserList;
import org.junit.jupiter.api.Assertions;
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
                .andExpect(jsonPath("$.eventName", is("第一条事件")))
                .andExpect(jsonPath("$.keyWord", is("无分类")))
                .andExpect(jsonPath("$", not(hasKey("userDto"))));
        mockMVC.perform(get("/rs/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第二条事件")))
                .andExpect(jsonPath("$.keyWord", is("无分类")))
                .andExpect(jsonPath("$", not(hasKey("userDto"))));
        mockMVC.perform(get("/rs/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("第三条事件")))
                .andExpect(jsonPath("$.keyWord", is("无分类")))
                .andExpect(jsonPath("$", not(hasKey("userDto"))));

    }

    @Test
    void should_get_rs_event_by_range() throws Exception {
        mockMVC.perform(get("/rs/event?start=1&end=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[0]", not(hasKey("userDto"))))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1]", not(hasKey("userDto"))))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2]", not(hasKey("userDto"))));

    }

    @Test
    void should_get_all_event() throws Exception {
        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[0]", not(hasKey("userDto"))))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1]", not(hasKey("userDto"))))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2]", not(hasKey("userDto"))));
    }

    @Test
    void should_add_one_rs_event() throws Exception {
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济");
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201))
                .andExpect(header().string("index", "3"));

        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
                .andExpect(jsonPath("$[3].keyWord", is("经济")));

    }

    @Test
    void should_modify_one_event() throws Exception {
        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")));

        mockMVC.perform(put("/rs/event?index=1&keyWord=哈哈"))
                .andExpect(status().isOk());

        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("哈哈")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")));

    }

    @Test
    void should_delete_one_event() throws Exception {
        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[2].keyWord", is("无分类")));

        mockMVC.perform(delete("/rs/event/2"))
                .andExpect(status().isOk());

        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
                .andExpect(jsonPath("$[1].eventName", is("第三条事件")))
                .andExpect(jsonPath("$[1].keyWord", is("无分类")));
    }


    @Test
    void should_not_add_one_rs_when_userName_is_null() throws Exception {

        UserDto userDto = new UserDto(null, 18, "男", "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void should_not_add_one_rs_when_size_of_userName_over_8() throws Exception {

        UserDto userDto = new UserDto("123456789", 18, "男", "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_user_age_less_than_18() throws Exception {

        UserDto userDto = new UserDto("12345678", 17, "男", "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_user_age_more_than_100() throws Exception {

        UserDto userDto = new UserDto("12345678", 101, "男", "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_user_gender_is_null() throws Exception {

        UserDto userDto = new UserDto("12345678", 100, null, "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_user_email_is_not_conform_shares() throws Exception {

        UserDto userDto = new UserDto("12345678", 100, "男", "12345678qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_user_phone_is_not_start_with_1() throws Exception {

        UserDto userDto = new UserDto("12345678", 100, "男", "12345678@qq.com", "22345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_size_of_user_phone_is_not_11() throws Exception {

        UserDto userDto = new UserDto("12345678", 100, "男", "12345678@qq.com", "123456789101");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_user_to_userList_When_user_is_registerd() throws Exception {

        UserDto user = new UserDto("guhao", 18, "男", "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
        int beforeSize = UserList.userList.size();
        Assertions.assertEquals(1, beforeSize);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));
        int afterSize = UserList.userList.size();
        Assertions.assertEquals(1, afterSize);
        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }

    @Test
    void should_not_add_one_user_to_userList() throws Exception {

        UserDto user = new UserDto("guhao1", 18, "男", "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
        int beforeSize = UserList.userList.size();
        Assertions.assertEquals(1, beforeSize);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));
        int afterSize = UserList.userList.size();
        Assertions.assertEquals(2, afterSize);
        mockMVC.perform(get("/rs/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }


    @Test
    void should_return_invalid_request_param_when_start_or_end_out_of_index() throws Exception {
        mockMVC.perform(get("/rs/event?start=-1&end=3"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error",is("invalid request param")));
    }

}