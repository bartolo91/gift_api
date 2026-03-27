package org.example.gift_api.mapper;

import org.example.gift_api.model.command.CreatePresentCommand;
import org.example.gift_api.model.command.UpdatePresentCommand;
import org.example.gift_api.model.dto.PresentDTO;
import org.example.gift_api.model.entity.Child;
import org.example.gift_api.model.entity.Present;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class PresentMapperTest {

    @Test
    void shouldMapFromCreateCommand() {
        CreatePresentCommand command = new CreatePresentCommand();
        command.setName("Lego");
        command.setPrice(BigDecimal.valueOf(99.99));

        Present present = PresentMapper.mapFromCommand(command);

        assertEquals("Lego", present.getName());
        assertEquals(BigDecimal.valueOf(99.99), present.getPrice());
    }

    @Test
    void shouldMapToDto() {
        Present present = Present.builder()
                .id(1L)
                .name("Puzzle")
                .price(BigDecimal.valueOf(15))
                .build();

        PresentDTO dto = PresentMapper.mapToDto(present);

        assertEquals(1L, dto.getId());
        assertEquals("Puzzle", dto.getName());
        assertEquals(BigDecimal.valueOf(15), dto.getPrice());
    }

    @Test
    void shouldUpdateAndCreateNewPresent() {
        UpdatePresentCommand command = new UpdatePresentCommand();
        command.setName("Hot Wheels Garage");
        command.setPrice(BigDecimal.valueOf(200));

        Child child = new Child();

        Present present = PresentMapper.updateFromCommand(command, 1L, child);

        assertEquals(1L, present.getId());
        assertEquals("Hot Wheels Garage", present.getName());
        assertEquals(BigDecimal.valueOf(200), present.getPrice());
        assertEquals(child, present.getChild());
    }

    @Test
    void shouldUpdateExistingPresent() {
        Present present = new Present();
        present.setName("Auta zdalnie sterowane: Ferrari");
        present.setPrice(BigDecimal.valueOf(150));

        UpdatePresentCommand command = new UpdatePresentCommand();
        command.setName("Auta zdalnie sterowane: Aston");
        command.setPrice(BigDecimal.valueOf(200));

        Present updated = PresentMapper.updateFromCommand(present, command);

        assertSame(present, updated);
        assertEquals("Auta zdalnie sterowane: Aston", updated.getName());
        assertEquals(BigDecimal.valueOf(200), updated.getPrice());
    }
}