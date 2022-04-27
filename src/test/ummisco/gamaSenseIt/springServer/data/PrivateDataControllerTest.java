package ummisco.gamaSenseIt.springServer.data;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import ummisco.gamaSenseIt.springServer.data.controller.PrivateDataController;
import ummisco.gamaSenseIt.springServer.security.SecurityUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PrivateDataController.class)
@ContextConfiguration(classes = {SecurityUtils.class})
public class PrivateDataControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void accessByIdSearch() throws Exception {
        RequestBuilder request = get("/private/accesses/search");
        MvcResult result = mvc.perform(request).andReturn();
        assertEquals(401, result.getResponse().getStatus());
    }
}
