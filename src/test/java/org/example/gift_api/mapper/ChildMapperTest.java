package org.example.gift_api.mapper;

import org.example.gift_api.model.command.CreateChildCommand;
import org.example.gift_api.model.command.UpdateChildCommand;
import org.example.gift_api.model.dto.ChildDTO;
import org.example.gift_api.model.entity.Child;
import org.example.gift_api.model.entity.Present;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ChildMapperTest {

    @Test
    void shouldMapFromCreateCommand() {
        CreateChildCommand command = new CreateChildCommand();
        command.setFirstName("Jan");
        command.setLastName("Kowalski");
        command.setBirthDate(LocalDate.of(2000, 1, 1));

        Child child = ChildMapper.mapFromCommand(command);

        assertEquals("Jan", child.getFirstName());
        assertEquals("Kowalski", child.getLastName());
        assertEquals(LocalDate.of(2000, 1, 1), child.getBirthDate());
    }

    @Test
    void shouldMapToDto() {
        Child child = Child.builder()
                .id(1L)
                .firstName("Paweł")
                .lastName("Nowak")
                .birthDate(LocalDate.of(2022, 1, 1))
                .presents(Set.of(new Present(), new Present()))
                .build();

        ChildDTO dto = ChildMapper.mapToDto(child);

        assertEquals(1L, dto.getId());
        assertEquals(2L, dto.getPresentsCount());
    }

    @Test
    void shouldUpdateChild() {
        Child child = new Child();
        UpdateChildCommand updatedChild = new UpdateChildCommand();
        updatedChild.setFirstName("Anna");

        Child updated = ChildMapper.updateFromCommand(child, updatedChild);

        assertEquals("Anna", updated.getFirstName());
    }
}