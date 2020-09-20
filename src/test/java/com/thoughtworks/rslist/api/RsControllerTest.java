package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VotesRepository;
import com.thoughtworks.rslist.utils.CommonUtils;
import org.aspectj.lang.annotation.After;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;


import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
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
    @Autowired
    VotesRepository votesRepository;

    @BeforeEach
    void initDataBase() {
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

        RsEvent rsEvent = RsEvent.builder().eventName("ceshi1")
                .keyWord("keyword")
                .userId(1)
                .userDto(userDto)
                .build();
        RsEventEntity rsEventEntity1 = CommonUtils.convertRsDtoToEntity(rsEvent);
        userEntity.setRsEvents(new ArrayList<RsEventEntity>());
        rsEventEntity1.setUserEntity(userEntity);
        rsEventRepository.save(rsEventEntity1);

        RsEvent rsEvent2 = RsEvent.builder().eventName("ceshi2")
                .keyWord("keyword")
                .userId(1)
                .userDto(userDto)
                .build();
        RsEventEntity rsEventEntity2 = CommonUtils.convertRsDtoToEntity(rsEvent2);
        rsEventEntity2.setUserEntity(userEntity);
        rsEventRepository.save(rsEventEntity2);
    }

    @AfterEach
    void clearDataBase() {
        userRepository.deleteAll();
        rsEventRepository.deleteAll();
        votesRepository.deleteAll();
    }

    @Test
    void should_get_one_rs_event() throws Exception {
        mockMVC.perform(get("/rsEvent/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventName", is("ceshi1")))
                .andExpect(jsonPath("$.keyword", is("keyword")))
                .andExpect(jsonPath("$", not(hasKey("userDto"))));
    }

    @Test
    void should_get_rs_event_by_range() throws Exception {
        mockMVC.perform(get("/rsEvent?start=1&end=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("ceshi1")))
                .andExpect(jsonPath("$[0].keyword", is("keyword")))
                .andExpect(jsonPath("$[0]", not(hasKey("userDto"))))
                .andExpect(jsonPath("$[1].eventName", is("ceshi2")))
                .andExpect(jsonPath("$[1].keyword", is("keyword")))
                .andExpect(jsonPath("$[1]", not(hasKey("userDto"))));

    }

    @Test
    void should_get_all_event() throws Exception {
        mockMVC.perform(get("/rsEvents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("ceshi1")))
                .andExpect(jsonPath("$[0].keyword", is("keyword")))
                .andExpect(jsonPath("$[0]", not(hasKey("userDto"))))
                .andExpect(jsonPath("$[1].eventName", is("ceshi2")))
                .andExpect(jsonPath("$[1].keyword", is("keyword")))
                .andExpect(jsonPath("$[1]", not(hasKey("userDto"))));
    }

    @Test
    void should_add_one_rs_event() throws Exception {

        UserDto user = new UserDto("guhao1", 18, "男", "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", user, 1);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(get("/rsEvents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
        mockMVC.perform(post("/rsEvent").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));

        mockMVC.perform(get("/rsEvents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));

    }

    @Test
    void should_modify_one_event() throws Exception {
        mockMVC.perform(get("/rsEvents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("ceshi1")))
                .andExpect(jsonPath("$[0].keyword", is("keyword")))
                .andExpect(jsonPath("$[0]", not(hasKey("userDto"))))
                .andExpect(jsonPath("$[1].eventName", is("ceshi2")))
                .andExpect(jsonPath("$[1].keyword", is("keyword")))
                .andExpect(jsonPath("$[1]", not(hasKey("userDto"))));

        mockMVC.perform(put("/rsEvent?index=1&keyWord=哈哈"))
                .andExpect(status().isOk());

        mockMVC.perform(get("/rsEvents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("ceshi1")))
                .andExpect(jsonPath("$[0].keyword", is("哈哈")))
                .andExpect(jsonPath("$[0]", not(hasKey("userDto"))))
                .andExpect(jsonPath("$[1].eventName", is("ceshi2")))
                .andExpect(jsonPath("$[1].keyword", is("keyword")))
                .andExpect(jsonPath("$[1]", not(hasKey("userDto"))));

    }


    @Test
    void should_delete_one_event() throws Exception {
        mockMVC.perform(get("/rsEvents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].eventName", is("ceshi1")))
                .andExpect(jsonPath("$[0].keyword", is("keyword")))
                .andExpect(jsonPath("$[0]", not(hasKey("userDto"))))
                .andExpect(jsonPath("$[1].eventName", is("ceshi2")))
                .andExpect(jsonPath("$[1].keyword", is("keyword")))
                .andExpect(jsonPath("$[1]", not(hasKey("userDto"))));

        mockMVC.perform(delete("/rsEvent/index/2"))
                .andExpect(status().isOk());

        mockMVC.perform(get("/rsEvents"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].eventName", is("ceshi1")))
                .andExpect(jsonPath("$[0].keyword", is("keyword")))
                .andExpect(jsonPath("$[0]", not(hasKey("userDto"))));
    }


    //按后面的需求，已经不需要对rsEvent里的User进行多余的校验，因为已经有userId来验证了。
    //没有在数据库里的userId不能对rsEvent进行操作
    /**
    @Test
    void should_not_add_one_rs_when_userName_is_null() throws Exception {

        UserDto userDto = new UserDto(null, 18, "男", "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto,1);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void should_not_add_one_rs_when_size_of_userName_over_8() throws Exception {

        UserDto userDto = new UserDto("123456789", 18, "男", "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto,1);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_user_age_less_than_18() throws Exception {

        UserDto userDto = new UserDto("12345678", 17, "男", "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto,1);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_user_age_more_than_100() throws Exception {

        UserDto userDto = new UserDto("12345678", 101, "男", "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto,1);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_user_gender_is_null() throws Exception {

        UserDto userDto = new UserDto("12345678", 100, null, "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto,1);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_user_email_is_not_conform_shares() throws Exception {

        UserDto userDto = new UserDto("12345678", 100, "男", "12345678qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto,1);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_user_phone_is_not_start_with_1() throws Exception {

        UserDto userDto = new UserDto("12345678", 100, "男", "12345678@qq.com", "22345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto,1);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_size_of_user_phone_is_not_11() throws Exception {

        UserDto userDto = new UserDto("12345678", 100, "男", "12345678@qq.com", "123456789101");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto,1);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rs/event").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
**/


    @Test
    void should_return_invalid_request_param_when_start_or_end_out_of_index() throws Exception {
        mockMVC.perform(get("/rsEvent?start=-1&end=3"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", is("invalid request param")));
    }

    @Test
    void should_return_invalid_request_param_when_index_is_not_invalid() throws Exception {
        mockMVC.perform(get("/rsEvent/0"))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error", is("invalid index")));
    }

    //TODO,userId的notnull失效，并不会报对其进行检验
    @Test
    void should_return_invalid_param_when_rsEvent_can_not_pass_valid() throws Exception {

        UserDto userDto = new UserDto("12345678910", 100, "男", "12345678@qq.com", "22345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rsEvent").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
              // .andExpect(jsonPath("$.error", is("invalid param")));
    }


    @Test
    void should_not_add_one_rs_when_rs_eventName_is_null() throws Exception {

        UserDto userDto = new UserDto("12345678", 100, "男", "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent(null, "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rsEvent").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_rs_eventName_keyWord_is_null() throws Exception {

        UserDto userDto = new UserDto("12345678", 100, "男", "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("", "经济", userDto);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rsEvent").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_rs_when_rs_user_is_null() throws Exception {

        UserDto userDto = new UserDto("12345678", 100, "男", "12345678@qq.com", "12345678910");
        RsEvent rsEvent = new RsEvent("猪肉涨价了", "经济", null);
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);
        mockMVC.perform(post("/rsEvent").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    void should_add_rsEvent_when_user_exist() throws Exception {

        RsEvent rsEvent = RsEvent.builder().eventName("热搜事件名称")
                .keyWord("关键字")
                .userId(1)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);

        mockMVC.perform(post("/rsEvent").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));
    }

    @Test
    void should_not_add_rsEvent_when_user_not_exist() throws Exception {


        RsEvent rsEvent = RsEvent.builder().eventName("热搜事件名称")
                .keyWord("关键字")
                .userId(2)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String rsEventJson = objectMapper.writeValueAsString(rsEvent);

        mockMVC.perform(post("/rsEvent").content(rsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400));
    }


    @Test
    void should_delete_all_rsEvent_when_userId_is_same() throws Exception {

        List<RsEventEntity> beForeDeleteRsEvents = rsEventRepository.findAll();
        assertEquals(2, beForeDeleteRsEvents.size());

        mockMVC.perform(delete("/rsEvent/userId/1"))
                .andExpect(status().is(200));
        List<RsEventEntity> rsEvents = rsEventRepository.findAll();
        assertEquals(0, rsEvents.size());
    }

    @Test
    void should_modify_rsEvent_when_userId_is_exsit() throws Exception {
        RsEvent modifyRsEvent = RsEvent.builder().eventName("modify")
                .userId(1)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String modifyRsEventJson = objectMapper.writeValueAsString(modifyRsEvent);

        mockMVC.perform(patch("/rsEvent/2").content(modifyRsEventJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        Optional<RsEventEntity> afterModifyRsEventEntity = rsEventRepository.findById(2);
        assertEquals("modify", afterModifyRsEventEntity.get().getEventName());


    }


    @Test
    void should_vote_for_rsEvent() throws Exception {
        Vote vote = Vote.builder()
                .voteNum(5)
                .userId(1)
                .voteTime(new Timestamp(System.currentTimeMillis()))
                .rsEventId(2)
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        String votesJson = objectMapper.writeValueAsString(vote);

        mockMVC.perform(post("/rsEvent/vote/2").content(votesJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));

        Optional<UserEntity> userEntityOptional = userRepository.findById(1);
        UserEntity modifiedUserEntity = userEntityOptional.get();
        Optional<RsEventEntity> rsEventEntityOptional = rsEventRepository.findById(2);
        RsEventEntity modifiedRsEventEntity = rsEventEntityOptional.get();
        assertEquals(5, modifiedUserEntity.getVoteNum());
        assertEquals(5, modifiedRsEventEntity.getVoteNum());

    }


}