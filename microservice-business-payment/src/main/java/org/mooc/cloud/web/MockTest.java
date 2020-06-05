package org.mooc.cloud.web;


import org.junit.Before;
import org.junit.runner.RunWith;
import org.mooc.cloud.PaymentEurekaApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {PaymentEurekaApplication.class})
public class MockTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }



    public void test() throws Exception {
        String content = mockMvc.perform(
                MockMvcRequestBuilders.post("/appapi/fullnews/getDetailnotLogin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("")
        ).andExpect(MockMvcResultMatchers.status()
                .isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        System.out.println(content);

    }
}
