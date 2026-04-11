package org.example.gift_api.controler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.gift_api.exceptions.GiftApiException;
import org.example.gift_api.model.command.CreateChildCommand;
import org.example.gift_api.model.command.CreatePresentCommand;
import org.example.gift_api.model.command.UpdateChildCommand;
import org.example.gift_api.model.command.UpdatePresentCommand;
import org.example.gift_api.model.dto.ChildDTO;
import org.example.gift_api.model.dto.PresentDTO;
import org.example.gift_api.model.entity.Child;
import org.example.gift_api.model.entity.Present;
import org.example.gift_api.repository.ChildRepository;
import org.example.gift_api.repository.PresentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.hasItems;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

    @Autowired
    private PresentRepository presentRepository;

    @BeforeEach
    void setup() {
        objectMapper.findAndRegisterModules();
        presentRepository.deleteAll();
        childRepository.deleteAll();
    }

    @Test
    void shouldCreateChild() throws Exception {
        //given:
        CreateChildCommand command = new CreateChildCommand();
        command.setFirstName("Jan");
        command.setLastName("Nowak");
        command.setBirthDate(LocalDate.of(1990, 1, 1));
        String json = objectMapper.writeValueAsString(command);

        //when:
        postman.perform(post("/api/v1/children")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.firstName").value(command.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(command.getLastName()))
                .andExpect(jsonPath("$.birthDate").value(command.getBirthDate().toString()))
                .andReturn()
                .getResponse();

        //then:
        assertEquals(1, childRepository.count());
        Child child = childRepository.findAll().get(0);
        assertEquals("Jan", child.getFirstName());
        assertEquals("Nowak", child.getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), child.getBirthDate());
    }

    @Test
    void shouldFailToCreateChildBecauseOfAllParametersAreNull() throws Exception {
        //given:
        CreateChildCommand command = new CreateChildCommand();
        String json = objectMapper.writeValueAsString(command);

        //when:
        postman.perform(post("/api/v1/children")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                // .andExpect(status().isBadRequest())
                // .andExpect(jsonPath("$.message").value("Validation errors")),
                // .andExpect(jsonPath("$.violations[0].field").value("firstName"))
                // .andExpect(jsonPath("$.violations[0].message").value("NO_VALUE"))
                // .andExpect(jsonPath("$.violations[1].field").value("lastName"))
                // .andExpect(jsonPath("$.violations[1].message").value("NO_VALUE"))
                // .andExpect(jsonPath("$.violations[2].field").value("birthDate"))
                // .andExpect(jsonPath("$.violations[2].message").value("NULL_VALUE"))
                .andExpect(jsonPath("$.violations[*].field").value(hasItems("firstName", "lastName", "birthDate")))
                .andExpect(jsonPath("$.violations[*].message").value(hasItems("NO_VALUE", "NO_VALUE", "NULL_VALUE")))
                .andReturn()
                .getResponse();
    }

    @Test
    void shouldFindChild() throws Exception {
        //given:
        Child child = new Child();
        child.setFirstName("Jan");
        child.setLastName("Nowak");
        child.setBirthDate(LocalDate.of(2000, 1, 1));

        child = childRepository.save(child);

        //when:
        postman.perform(get("/api/v1/children/" + child.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jan"))
                .andExpect(jsonPath("$.lastName").value("Nowak"))
                .andExpect(jsonPath("$.birthDate").value("2000-01-01"));
    }

    @Test
    void shouldNotFindChildAndThrowEntityNotFoundException() throws Exception {
        //given:
        int childId = 2500;

        //when:
        //then:
        try {
            postman.perform(get("/api/v1/children" + childId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(""))
                    .andExpect(status().isNotFound())
                    .andReturn()
                    .getResponse();
        } catch (GiftApiException e) {
            assertEquals(e.getMessage(), "ENTITY_NOT_FOUND ID: " + childId);
        }
    }

    @Test
    void shouldNotFindChildAndReturn404() throws Exception {
        int childId = 2500;

        postman.perform(get("/api/v1/children/" + childId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("ENTITY_NOT_FOUND ID: " + childId));
    }

    @Test
    void shouldDeleteChild() throws Exception {
        Child child = new Child();
        child.setFirstName("Jan");
        child.setLastName("Nowak");
        child.setBirthDate(LocalDate.of(2000, 1, 1));

        child = childRepository.save(child);

        postman.perform(delete("/api/v1/children/" + child.getId()))
                .andExpect(status().isNoContent());

        assertFalse(childRepository.findById(child.getId()).isPresent());
    }

    @Test
    void shouldUpdateChild() throws Exception { // TODO pluje sie o wersje
        Child child = new Child();
        child.setFirstName("Jan");
        child.setLastName("Nowak");
        child.setBirthDate(LocalDate.of(2000, 1, 1));

        child = childRepository.save(child);

        UpdateChildCommand update = new UpdateChildCommand();
        update.setVersion(child.getVersion());
        update.setFirstName("Janusz");
        update.setLastName("Nowakowski");
        update.setBirthDate(LocalDate.of(2000, 1, 1));

        String json = objectMapper.writeValueAsString(update);

        // when
        postman.perform(put("/api/v1/children/" + child.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Janusz"))
                .andExpect(jsonPath("$.lastName").value("Nowakowski"))
                .andExpect(jsonPath("$.birthDate").value("2000-01-01"));


        // then
        assertEquals(1, childRepository.count());
        Child updatedChild = childRepository.findAll().get(0);

        assertEquals("Janusz", updatedChild.getFirstName());
        assertEquals("Nowakowski", updatedChild.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), updatedChild.getBirthDate());
    }

    @Test
    void shouldAddAndGetAndRemovePresent() throws Exception {
        Child child = new Child();
        child.setFirstName("Jan");
        child.setLastName("Nowak");
        child.setBirthDate(LocalDate.of(2000, 1, 1));

        child = childRepository.save(child);

        CreatePresentCommand present = new CreatePresentCommand();
        present.setName("Hot Wheels");
        present.setPrice(BigDecimal.valueOf(100));

        // add present
        String presentJson = objectMapper.writeValueAsString(present);

        String response = postman.perform(post("/api/v1/children/" + child.getId() + "/presents")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(presentJson))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PresentDTO addedPresent = objectMapper.readValue(response, PresentDTO.class);

        assertEquals("Hot Wheels", addedPresent.getName());
        assertEquals(BigDecimal.valueOf(100), addedPresent.getPrice());

        String allPresentsJson = postman.perform(get("/api/v1/children/" + child.getId() + "/presents"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        List<PresentDTO> presents = objectMapper.readValue(
                allPresentsJson,
                new TypeReference<List<PresentDTO>>() {
                }
        );

        assertEquals(1, presents.size());
        PresentDTO firstPresent = presents.get(0);
        assertEquals(addedPresent.getId(), firstPresent.getId());
        assertEquals("Hot Wheels", firstPresent.getName());
        assertEquals(0, firstPresent.getPrice().compareTo(BigDecimal.valueOf(100)));

        // delete
        postman.perform(delete("/api/v1/children/" + child.getId() + "/presents/" + addedPresent.getId()))
                .andExpect(status().isNoContent());

        assertEquals(0, presentRepository.count());
    }

    @Test
    void shouldUpdatePresent() throws Exception {
        Child child = new Child();
        child.setFirstName("Jan");
        child.setLastName("Nowak");
        child.setBirthDate(LocalDate.of(2000, 1, 1));

        child = childRepository.save(child);

        Present present = new Present();
        present.setName("Lego 1");
        present.setPrice(BigDecimal.valueOf(200));
        present.setChild(child);
        present = presentRepository.save(present);

        UpdatePresentCommand updatePresent = new UpdatePresentCommand();
        updatePresent.setName("Lego 2");
        updatePresent.setPrice(BigDecimal.valueOf(300));

        String json = objectMapper.writeValueAsString(updatePresent);

        postman.perform(put("/api/v1/children/" + child.getId() + "/presents/" + present.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(present.getId()))
                .andExpect(jsonPath("$.name").value("Lego 2"))
                .andExpect(jsonPath("$.price").value(300));
    }

    @Test
    void shouldThrowOptimisticLockingFailureException() {
        // given
        Child child = new Child();
        child.setFirstName("Jan");
        child.setLastName("Nowak");
        child.setBirthDate(LocalDate.of(2000, 1, 1));

        child = childRepository.save(child);

        Child childThread1 = childRepository.findById(child.getId()).get();
        Child childThread2 = childRepository.findById(child.getId()).get();

        childThread1.setFirstName("Kamil");
        childRepository.saveAndFlush(childThread1);

        childThread2.setFirstName("Piotr");

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            childRepository.saveAndFlush(childThread2);
        });
    }
}
