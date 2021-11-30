package ru.javawebinar.topjava.web.resource;

import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.web.common.AbstractControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ResourceControllerTest extends AbstractControllerTest {
    @Test
    void getStyle() throws Exception {
        perform(get("/resources/css/style.css"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().stringValues("Content-Type", "text/css;charset=UTF-8"));
    }
}
