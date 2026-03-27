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
import org.example.gift_api.service.ChildService;
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
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

    @Autowired
    private PresentRepository presentRepository;
    @Autowired
    private ChildService childService;

    @BeforeEach
    void setup() {
        objectMapper.findAndRegisterModules();
        presentRepository.deleteAll();
        childRepository.deleteAll();
    }

    @Test
    void shouldCreateChild() throws Exception { // TODO COS BY TRZEBA POPRAWIC
        //given:
        CreateChildCommand command = new CreateChildCommand();
        command.setFirstName("Jan");
        command.setLastName("Nowak");
        command.setBirthDate(LocalDate.of(1990, 1, 1));
        String json = objectMapper.writeValueAsString(command);

        //when:
        MockHttpServletResponse response = postman.perform(post("/api/v1/children")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
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
        String responseJson = postman.perform(post("/api/v1/children")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();

        //then:
        List<String> errorCodes = objectMapper.readerForListOf(String.class)
                .readValue(responseJson);

        assertTrue(errorCodes.containsAll(Arrays.asList("FIRST_NAME_NOT_EMPTY_OR_NULL", "LAST_NAME_NOT_EMPTY_OR_NULL", "BIRTH_DATE_NOT_NULL")));
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
        String responseJson = postman.perform(get("/api/v1/children/" + child.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ChildDTO childDTO = objectMapper.readValue(responseJson, ChildDTO.class);

        assertEquals("Jan", childDTO.getFirstName());
        assertEquals("Nowak", childDTO.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), childDTO.getBirthDate());
    }

    @Test
    void shouldNotFindChildAndThrowEntityNotFoundException() throws Exception {
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
        } catch (GiftApiException e) {
            assertEquals(e.getMessage(), "ENTITY_NOT_FOUND ID: " + childId);
        }
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

        assertEquals(0, childRepository.count());
    }

    @Test
    void shouldUpdateChild() throws Exception {
        Child child = new Child();
        child.setFirstName("Jan");
        child.setLastName("Nowak");
        child.setBirthDate(LocalDate.of(2000, 1, 1));

        child = childRepository.save(child);

        UpdateChildCommand update = new UpdateChildCommand();
        update.setFirstName("Janusz");
        update.setLastName("Nowakowski");
        update.setBirthDate(LocalDate.of(2000, 1, 1));

        String json = objectMapper.writeValueAsString(update);

        // when
        String responseJson = postman.perform(put("/api/v1/children/" + child.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        // then
        ChildDTO updatedChild = objectMapper.readValue(responseJson, ChildDTO.class);

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
                new TypeReference<List<PresentDTO>>() {}
        );

        assertEquals(1, presents.size());
        PresentDTO firstPresent = presents.get(0);
        assertEquals(addedPresent.getId(), firstPresent.getId());
        assertEquals("Hot Wheels", firstPresent.getName());
        assertEquals(0, firstPresent.getPrice().compareTo(BigDecimal.valueOf(100)));

        // delete
        postman.perform(delete("/api/v1/children/" + child.getId() + "/presents/" + addedPresent.getId()))
                .andExpect(status().isNoContent());

        assertEquals(0, presentRepository.count()); // orphanRemoval = true)??
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

        String responseJson = postman.perform(put("/api/v1/children/" + child.getId() + "/presents/" + present.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        PresentDTO updatedPresent = objectMapper.readValue(responseJson, PresentDTO.class);

        assertEquals(present.getId(), updatedPresent.getId());
        assertEquals("Lego 2", updatedPresent.getName());
        assertEquals(BigDecimal.valueOf(300), updatedPresent.getPrice());
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

//    @Test
//    void pessimisticLocking_shouldBlockOtherTransaction() throws Exception {
//        // given
//        Child child = new Child();
//        child.setFirstName("Jan");
//        child.setLastName("Nowak");
//        child.setBirthDate(LocalDate.of(2000, 1, 1));
//
//        child = childRepository.saveAndFlush(child);
//        final Child finalChild = child;
//
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//
//        Future<Long> t1 = executor.submit(() -> {
//            childService.findByIdWIthPessimisticLocking(finalChild.getId());
//            Thread.sleep(2000); // symulacja pracy z zablokowaną encją
//            return System.currentTimeMillis();
//        });
//
//        Future<Long> t2 = executor.submit(() -> {
//            long start = System.currentTimeMillis();
//            childService.findByIdWIthPessimisticLocking(finalChild.getId());
//            long end = System.currentTimeMillis();
//            return end - start;
//        });
//
//        long duration = t2.get();
//        System.out.println("Thread 2 waited: " + duration + "ms");
//
//        assertTrue(duration >= 1900, "Second thread should wait for first thread");
//    }
}
