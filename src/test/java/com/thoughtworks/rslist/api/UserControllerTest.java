package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.dto.RsEvent;
import com.thoughtworks.rslist.dto.UserDto;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {

    @Autowired
    MockMvc mockMVC;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void initDataBase (){
        userRepository.deleteAll();
    }
    @Test
    void should_not_add_one_user_when_user_name_is_null() throws Exception {

        UserDto userDto = new UserDto(null,100,"男","12345678@qq.com","12345678910");

        ObjectMapper objectMapper=new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDto);
        mockMVC.perform(post("/user/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void should_not_add_one_user_when_size_of_user_name_is_more_than_8() throws Exception {

        UserDto userDto = new UserDto("123456789",100,"男","12345678@qq.com","12345678910");

        ObjectMapper objectMapper=new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDto);
        mockMVC.perform(post("/user/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_user_when_user_age_is_over_100() throws Exception {

        UserDto userDto = new UserDto("12345678",101,"男","12345678@qq.com","12345678910");

        ObjectMapper objectMapper=new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDto);
        mockMVC.perform(post("/user/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_user_when_user_age_is_less_than_18() throws Exception {

        UserDto userDto = new UserDto("12345678",17,"男","12345678@qq.com","12345678910");

        ObjectMapper objectMapper=new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDto);
        mockMVC.perform(post("/user/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_user_when_user_gender_is_null() throws Exception {

        UserDto userDto = new UserDto("12345678",18,null,"12345678@qq.com","12345678910");

        ObjectMapper objectMapper=new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDto);
        mockMVC.perform(post("/user/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_not_add_one_user_when_user_email_is_not_conform_shares() throws Exception {

        UserDto userDto = new UserDto("12345678",18,null,"12345678@qq.com","12345678910");

        ObjectMapper objectMapper=new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDto);
        mockMVC.perform(post("/user/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }




    @Test
    void should_not_add_one_user_when_user_phone_is_not_start_with_1() throws Exception {

        UserDto userDto = new UserDto("12345678",100,"男","12345678@qq.com","22345678910");

        ObjectMapper objectMapper=new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDto);
        mockMVC.perform(post("/user/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
    @Test
    void should_not_add_one_user_when_size_of_user_phone_is_not_11() throws Exception {

        UserDto userDto = new UserDto("12345678",100,"男","12345678@qq.com","123456789101");

        ObjectMapper objectMapper=new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDto);
        mockMVC.perform(post("/user/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void should_add_one_user_() throws Exception {

        UserDto userDto = new UserDto("12345678",100,"男","12345678@qq.com","12345678910");
        ObjectMapper objectMapper=new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDto);
        mockMVC.perform(post("/user/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void should_get_all_users() throws Exception {
        mockMVC.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].user_name",is("guhao")))
                .andExpect(jsonPath("$[0].user_age",is(18)))
                .andExpect(jsonPath("$[0].user_gender",is("男")))
                .andExpect(jsonPath("$[0].user_email",is("12345678@qq.com")))
                .andExpect(jsonPath("$[0].user_phone",is("12345678910")));
    }

    @Test
    void should_return_invalid_user_when_user_can_not_pass_valid() throws Exception {

        UserDto userDto = new UserDto("12345678123124",100,"男","12345678@qq.com","12345678910");
        ObjectMapper objectMapper=new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDto);
        mockMVC.perform(post("/user/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$.error",is("invalid user")));
    }

    @Test
    void should_add_user_to_Mysql() throws Exception {

        UserDto userDto = new UserDto("12345678",100,"男","12345678@qq.com","12345678910",10);
        ObjectMapper objectMapper=new ObjectMapper();
        String userJson = objectMapper.writeValueAsString(userDto);
        mockMVC.perform(post("/user/register").content(userJson).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        List<UserEntity> users = userRepository.findAll();
        assertEquals(1,users.size());
        assertEquals("12345678",users.get(0).getUserNmae());
    }


}