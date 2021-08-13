package pizza.controllers;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@WebMvcTest(DesignPizzaController.class)
public class TestDesignPizzaController {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void showDesignForm() throws Exception {
         mockMvc
                 .perform(MockMvcRequestBuilders.get("/design"))
                 .andExpect(MockMvcResultMatchers.status().isOk())
                 .andExpect(MockMvcResultMatchers.view().name("design"))
                 .andExpect(MockMvcResultMatchers.content().string(
                         Matchers.containsString("Yeast Dough"))
                 );
    }
}