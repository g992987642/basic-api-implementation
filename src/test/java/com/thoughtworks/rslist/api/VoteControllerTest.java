package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.dto.Vote;
import com.thoughtworks.rslist.entity.RsEventEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.repository.RsEventRepository;
import com.thoughtworks.rslist.repository.UserRepository;
import com.thoughtworks.rslist.repository.VotesRepository;
import com.thoughtworks.rslist.utils.CommonUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class VoteControllerTest {


    @Autowired
    MockMvc mockMVC;
    @Autowired
    UserRepository userRepository;
    @Autowired
    RsEventRepository rsEventRepository;
    @Autowired
    VotesRepository votesRepository;


    Timestamp tempDate;

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

        Vote vote = Vote.builder()
                .voteNum(1)
                .userId(1)
                .voteTime(new Timestamp(System.currentTimeMillis()+10000))
                .rsEventId(2)
                .build();
        VoteEntity voteEntity = CommonUtils.converVotesToEntity(vote, 2);
        votesRepository.save(voteEntity);

        Vote vote1 = Vote.builder()
                .voteNum(2)
                .userId(1)
                .voteTime(new Timestamp(System.currentTimeMillis()))
                .rsEventId(2)
                .build();
        VoteEntity voteEntity1 = CommonUtils.converVotesToEntity(vote1, 2);
        votesRepository.save(voteEntity1);

        tempDate = new Timestamp((System.currentTimeMillis()+20000));
        Vote vote2 = Vote.builder()
                .voteNum(3)
                .userId(1)
                .voteTime(tempDate)
                .rsEventId(2)
                .build();
        VoteEntity voteEntity2 = CommonUtils.converVotesToEntity(vote2, 2);
        votesRepository.save(voteEntity2);



        Vote vote3 = Vote.builder()
                .voteNum(4)
                .userId(1)
                .voteTime(new Timestamp(System.currentTimeMillis()+30000))
                .rsEventId(2)
                .build();
        VoteEntity voteEntity3 = CommonUtils.converVotesToEntity(vote3, 2);
        votesRepository.save(voteEntity3);

        Vote vote4 = Vote.builder()
                .voteNum(5)
                .userId(1)
                .voteTime(new Timestamp(System.currentTimeMillis()+40000))
                .rsEventId(2)
                .build();
        VoteEntity voteEntity4 = CommonUtils.converVotesToEntity(vote4, 2);
        votesRepository.save(voteEntity4);
    }

    @AfterEach
    void clearDataBase() {
        votesRepository.deleteAll();
    }

    @Test
    void should_get_votes_between_start_and_end() throws Exception {

        ObjectMapper objectMapper = new ObjectMapper();
        String start = objectMapper.writeValueAsString(tempDate);
        String end = objectMapper.writeValueAsString( new Timestamp((System.currentTimeMillis()+100000)));
        mockMVC.perform(get("/votes?start="+start+"&end="+end))
                .andExpect(jsonPath("$", hasSize(3)));

    }
}
