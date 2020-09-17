package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.UserList;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.utils.CommonUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsControllerTest {

    @Autowired
    MockMvc mockMVC;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;

    @BeforeEach
    void initDataBase() {
        rsEventRepository.deleteAll();
        userRepository.deleteAll();
    }

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

//    @Test
//    void should_add_one_rs_event() throws Exception {
//
//        UserDto user = new UserDto("guhao1", 18, "男", "12345678@qq.com", "12345678910");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
//        mockMVC.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)));
//        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is(201))
//                .andExpect(header().string("index", "3"));
//
//        mockMVC.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(4)))
//                .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
//                .andExpect(jsonPath("$[0].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
//                .andExpect(jsonPath("$[1].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[2].eventName", is("第三条事件")))
//                .andExpect(jsonPath("$[2].keyWord", is("无分类")))
//                .andExpect(jsonPath("$[3].eventName", is("猪肉涨价了")))
//                .andExpect(jsonPath("$[3].keyWord", is("经济")));
//
//    }

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

//    @Test
//    void should_not_add_one_user_to_userList_When_user_is_registerd() throws Exception {
//
//        UserDto user = new UserDto("guhao", 18, "男", "12345678@qq.com", "12345678910");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
//        mockMVC.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)));
//        int beforeSize = UserList.userList.size();
//        Assertions.assertEquals(1, beforeSize);
//        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is(201));
//        int afterSize = UserList.userList.size();
//        Assertions.assertEquals(1, afterSize);
//        mockMVC.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(4)));
//    }

//    @Test
//    void should_not_add_one_user_to_userList() throws Exception {
//
//        UserDto user = new UserDto("guhao1", 18, "男", "12345678@qq.com", "12345678910");
//        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user);
//        ObjectMapper objectMapper = new ObjectMapper();
//        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
//        mockMVC.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(3)));
//        int beforeSize = UserList.userList.size();
//        Assertions.assertEquals(1, beforeSize);
//        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
//                .andExpect(status().is(201));
//        int afterSize = UserList.userList.size();
//        Assertions.assertEquals(2, afterSize);
//        mockMVC.perform(get("/rs/list"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$", hasSize(4)));
//    }


    @Test
    void should_return_invalid_request_param_when_start_or_end_out_of_index() throws Exception {
        mockMVC.perform(get("/rs/event?start=-1&end=3"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    void should_return_invalid_request_param_when_index_is_not_invalid() throws Exception {
        mockMVC.perform(get("/rs/0"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", is("invalid index")));
    }

    @Test
    void should_return_invalid_param_when_rsEvent_can_not_pass_valid() throws Exception {

        UserDto userDto = new UserDto("12345678910", 100, "男", "12345678@qq.com", "22345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", is("invalid param")));
    }


    @Test
    void should_not_add_one_rs_when_rs_eventName_is_null() throws Exception {

        UserDto userDto = new UserDto("12345678", 100, "男", "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent(null, "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_rs_eventName_keyWord_is_null() throws Exception {

        UserDto userDto = new UserDto("12345678", 100, "男", "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_rs_user_is_null() throws Exception {

        UserDto userDto = new UserDto("12345678", 100, "男", "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", null);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void should_add_rsEvent_when_user_exist() throws Exception {
        UserDto userDto = UserDto.builder()
                .userName("guhao")
                .age(18)
                .email("1234123@qq.com")
                .gender("男")
                .phone("12345678910")
                .voteNum(10)
                .build();
        UserEntity userEntity = CommonUtils.convertUserDtoToEntity(userDto);
        userRepository.save(userEntity);

      RsEvent rsEvent=  RsEvent.builder().eventName("热搜事件名称")
                .keyWord("关键字")
                .userId(1)
                .userDto(userDto)
                .build();

        ObjectMapper objectMapper=new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);

        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));
    }

    @Test
    void should_not_add_rsEvent_when_user_not_exist() throws Exception {
        UserDto userDto = UserDto.builder()
                .userName("guhao")
                .age(18)
                .email("1234123@qq.com")
                .gender("男")
                .phone("12345678910")
                .voteNum(10)
                .build();

        RsEvent rsEvent=  RsEvent.builder().eventName("热搜事件名称")
                .keyWord("关键字")
                .userId(1)
                .userDto(userDto)
                .build();

        ObjectMapper objectMapper=new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);

        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }



    @Test
    void should_delete_all_rsEvent_when_userId_is_same() throws Exception {
        UserDto userDto = UserDto.builder()
                .userName("guhao")
                .age(18)
                .email("1234123@qq.com")
                .gender("nan")
                .phone("12345678910")
                .voteNum(10)
                .build();
        UserEntity userEntity = CommonUtils.convertUserDtoToEntity(userDto);
        userRepository.save(userEntity);

        RsEvent rsEvent=  RsEvent.builder().eventName("ceshi1")
                .keyWord("keyword")
                .userId(1)
                .userDto(userDto)
                .build();
        RsEventEntity rsEventEntity1 = CommonUtils.converRsDtoToEntity(rsEvent);
        userEntity.setRsEvents(new ArrayList<RsEventEntity>());
        rsEventEntity1.setUserEntity(userEntity);
        rsEventRepository.save(rsEventEntity1);

        RsEvent rsEvent2=  RsEvent.builder().eventName("ceshi2")
                .keyWord("keyword")
                .userId(1)
                .userDto(userDto)
                .build();
        RsEventEntity rsEventEntity2 = CommonUtils.converRsDtoToEntity(rsEvent2);
        rsEventEntity2.setUserEntity(userEntity);
        rsEventRepository.save(rsEventEntity2);


        List<RsEventEntity> beForeDeleteRsEvents = rsEventRepository.findAll();
        assertEquals(2,beForeDeleteRsEvents.size());

        mockMVC.perform(delete("/rs/event/1"))
                .andExpect(status().is(200));
        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        assertEquals(0,rsEvents.size());
    }

    @Test
    void should_modify_rsEvent_when_userId_is_exsit() throws Exception {
        UserDto userDto = UserDto.builder()
                .userName("guhao")
                .age(18)
                .email("1234123@qq.com")
                .gender("nan")
                .phone("12345678910")
                .voteNum(10)
                .build();
        UserEntity userEntity = CommonUtils.convertUserDtoToEntity(userDto);
        userRepository.save(userEntity);

        RsEvent rsEvent=  RsEvent.builder().eventName("ceshi1")
                .keyWord("keyword")
                .userId(1)
                .userDto(userDto)
                .build();
        RsEventEntity rsEventEntity1 = CommonUtils.converRsDtoToEntity(rsEvent);
        userEntity.setRsEvents(new ArrayList<RsEventEntity>());
        rsEventEntity1.setUserEntity(userEntity);
        rsEventRepository.save(rsEventEntity1);

        RsEvent rsEvent2=  RsEvent.builder().eventName("ceshi2")
                .keyWord("keyword")
                .userId(1)
                .userDto(userDto)
                .build();
        RsEventEntity rsEventEntity2 = CommonUtils.converRsDtoToEntity(rsEvent2);
        rsEventEntity2.setUserEntity(userEntity);
        rsEventRepository.save(rsEventEntity2);

        RsEvent modifyRsEvent=  RsEvent.builder().eventName("modify")
                .userId(1)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String modifyRsEventJson = objectMapper.writeValueAsString(modifyRsEvent);


        mockMVC.perform(patch("/rs/2").content(modifyRsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        Optional<RsEventEntity> afterModifyRsEventEntity= rsEventRepository.findById(2);
        assertEquals("modify",afterModifyRsEventEntity.get().getEventName());


    }



}