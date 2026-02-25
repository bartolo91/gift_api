package org.example.gift_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.gift_api.GiftApiApplication;
import org.example.gift_api.model.command.CreateChildCommand;
import org.example.gift_api.repository.ChildRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = GiftApiApplication.class)
@AutoConfigureMockMvc
class ChildServiceTest {

    private MockMvc postman;
    private ObjectMapper objectMapper;
    @Autowired
    private ChildRepository doctorRepository;

    @Test
    void shouldCreateChild() throws Exception {
        //given:
        CreateChildCommand command = new CreateChildCommand();
        command.setFirstName("Jan");
        command.setLastName("Kowalski");
        command.setBirthDate(LocalDate.of(2020, 1, 1));
        String json = objectMapper.writeValueAsString(command);
        //when:
        MockHttpServletResponse mockHttpServletResponse = postman.perform(post("/children")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(json)
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        //then:
        assertNull(mockHttpServletResponse.getErrorMessage());
    }
}