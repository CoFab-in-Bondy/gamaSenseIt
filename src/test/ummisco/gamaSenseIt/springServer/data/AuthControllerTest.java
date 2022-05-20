package ummisco.gamaSenseIt.springServer.data;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ummisco.gamaSenseIt.springServer.data.controller.AuthController;
import ummisco.gamaSenseIt.springServer.security.SecurityUtils;

import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void login() throws Exception {
        String username = "nonono";
        String password = "nonono";
        String body = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", username, password);

        MvcResult result = mvc.perform(
                post("/auth/login")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON)
                ).andExpect(status().isOk()).andReturn();

        String token = Pattern.compile("\\{\s+\"token\"\s+:\s+\"(.+)\"}")
                .matcher(result.getResponse().getContentAsString())
                .results()
                .map(m->m.group(0))
                .findFirst()
                .orElse("");

        mvc.perform(
                get("/auth/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk()
        );
    }
}
