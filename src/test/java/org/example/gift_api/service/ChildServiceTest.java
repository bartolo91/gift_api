package org.example.gift_api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.gift_api.GiftApiApplication;
import org.example.gift_api.model.command.CreateChildCommand;
import org.example.gift_api.model.dto.ChildDTO;
import org.example.gift_api.model.entity.Child;
import org.example.gift_api.repository.ChildRepository;
import org.example.gift_api.repository.PresentRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = GiftApiApplication.class)
@AutoConfigureMockMvc
class ChildServiceTest {

    @Mock
    private ChildRepository childRepository;

    @Mock
    private PresentRepository presentRepository;

    @InjectMocks
    private ChildService childService;

    @Test
    void shouldReturnChildById() {
        // given
        Child child = new Child();
        child.setId(1L);

        when(childRepository.findById(1L))
                .thenReturn(Optional.of(child));

        // when
        ChildDTO result = childService.getChildById(1L);

        // then
        assertNotNull(result);
        verify(childRepository).findById(1L);
    }
}