package cxcheng.salary;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private PersonService personService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void contextLoads() {
        assertThat(personService).isNotNull();
    }

    @Test
    public void badURLs() throws Exception {
        this.mockMvc.perform(get("/")).andExpect(status().isNotFound());
        this.mockMvc.perform(get("/user")).andExpect(status().isNotFound());
    }

    @Test
    public void users() throws Exception {
        assertTrue(personService.readCSV(this.getClass().getResourceAsStream("/us_presidents.csv")) > 0);
        MvcResult result = this.mockMvc.perform(get("/users")).andExpect(status().isOk()).
                andReturn();
        var usersJson = (new JSONObject(result.getResponse().getContentAsString())).getJSONArray("results");
        for (int i = 0; i < usersJson.length(); ++i) {
            JSONObject userJson = usersJson.getJSONObject(i);
            assertTrue(userJson.has("name") && userJson.has("salary") && !userJson.has("id"));
            Person p = new Person();
            p.setName(userJson.getString("name"));
            p.setSalary(userJson.getDouble("salary"));
            assertTrue(p.hasValidSalary());
        }
    }

}
