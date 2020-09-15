package com.thoughtworks.rslist.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RsControllerTest {

    @Autowired
    MockMvc mockMVC;

    @Test
    void should_get_one_rs_event() throws Exception {
        mockMVC.perform(get("/rs/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("第一条事件"));
        mockMVC.perform(get("/rs/2"))
                .andExpect(status().isOk())
                .andExpect(content().string("第二条事件"));
        mockMVC.perform(get("/rs/3"))
                .andExpect(status().isOk())
                .andExpect(content().string("第三条事件"));

    }

}