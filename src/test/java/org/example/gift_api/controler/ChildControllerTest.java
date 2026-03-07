package org.example.gift_api.controler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.gift_api.exceptions.types.EntityNotFoundException;
import org.example.gift_api.model.command.CreateChildCommand;
import org.example.gift_api.repository.ChildRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ChildControllerTest {

    @Autowired
    private MockMvc postman;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ChildRepository childRepository;

    @Test
    void shouldCreateChild() throws Exception {
        //given:
        CreateChildCommand command = new CreateChildCommand();
        command.setFirstName("Jan");
        command.setLastName("Nowak");
        command.setBirthDate(LocalDate.of(1990, 1, 1));
        String json = objectMapper.writeValueAsString(command);
        //when:
        MockHttpServletResponse mockHttpServletResponse = postman.perform(post("/api/v1/children")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse();
        //then:
        assertNull(mockHttpServletResponse.getErrorMessage());
    }

    @Test
    void shouldFailToCreateChildBecauseOfAllParametersAreNull() throws Exception {
        //given:
        CreateChildCommand command = new CreateChildCommand();
        String json = objectMapper.writeValueAsString(command);
        //when:
        String responseJson = postman.perform(post("/api/v1/children")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        //then:
        Map<String, String> errors = objectMapper.readValue(
                responseJson,
                new TypeReference<Map<String, String>>() {});

        assertEquals(errors.get("firstName"), "FIRST_NAME_NOT_EMPTY");
        assertEquals(errors.get("lastName"), "LAST_NAME_NOT_NULL");
        assertEquals(errors.get("birthDate"), "BIRTH_DATE_NOT_NULL");
    }

    @Test
    void shouldFindSingleChild() throws Exception {
        //given:
        int childId = 1;
        String json = objectMapper.writeValueAsString(childRepository.findById((long) childId));
        //when:
        MockHttpServletResponse mockHttpServletResponse = postman.perform(get("children" + childId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();
        //then:
        assertNull(mockHttpServletResponse.getErrorMessage());
    }

    @Test
    void shouldNotFindSingleChildAndThrowEntityNotFoundException() throws Exception {
        //given:
        int childId = 2500;
        //when:
        //then:
        try {
            MockHttpServletResponse mockHttpServletResponse = postman.perform(get("/api/v1/children" + childId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(""))
                    .andExpect(status().isNotFound())
                    .andReturn()
                    .getResponse();
        } catch (EntityNotFoundException e) {
            assertEquals(e.getMessage(), "ENTITY_NOT_FOUND ID: " + childId);
        }
    }

//    @Test
//    void shouldFindAllChildren() throws Exception {
//        //given:
//        String json = objectMapper.writeValueAsString(childRepository.findAll());
//        //when:
//        MockHttpServletResponse mockHttpServletResponse = postman.perform(get("/api/v1/children")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(json))
//                .andExpect(status().isOk())
//                .andReturn()
//                .getResponse();
//        //then:
//        assertNull(mockHttpServletResponse.getErrorMessage());
//    }

}