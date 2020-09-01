package com.au.superchoice.registration.controller;

import com.au.superchoice.registration.service.MemberRegistrationsService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = {MemberRegistrationsController.class})
@RunWith(SpringRunner.class)
public class MemberRegistrationsControllerTest {
    @MockBean
    private MemberRegistrationsService memberRegistrationsService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetGroupedByMemberRegistrations() throws Exception {
        MockHttpServletRequestBuilder getResult = MockMvcRequestBuilders.get("/member");
        ResultActions actualPerformResult = this.mockMvc
                .perform(getResult.param("file", String.valueOf((Object) null)).param("groupBy", "foo"));
        actualPerformResult.andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}

