package org.example.gift_api.service;

import org.example.gift_api.model.command.CreateChildCommand;
import org.example.gift_api.model.command.UpdateChildCommand;
import org.example.gift_api.model.dto.ChildDTO;
import org.example.gift_api.model.entity.Child;
import org.example.gift_api.model.entity.ChildView;
import org.example.gift_api.repository.ChildRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChildServiceTest {

    @Mock
    private ChildRepository childRepository;

    @Mock
    private PresentService presentService;

    @InjectMocks
    private ChildService childService;

    @Test
    void shouldCreateChild() {
        CreateChildCommand command = new CreateChildCommand();
        command.setFirstName("Jan");

        Child saved = new Child();
        saved.setFirstName("Jan");

        when(childRepository.save(any())).thenReturn(saved);

        ChildDTO result = childService.createChild(command);

        assertEquals("Jan", result.getFirstName());
        verify(childRepository).save(any());
    }

    @Test
    void shouldGetChildById() {
        Long id = 1L;

        Child child = new Child();
        child.setId(id);

        when(childRepository.findByIdWithPresentCount(id))
                .thenReturn(Optional.of(child));

        ChildDTO result = childService.getChildById(id);

        assertEquals(id, result.getId());
    }

    @Test
    void shouldThrowWhenChildNotFound() {
        when(childRepository.findByIdWithPresentCount(1L))
                .thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> childService.getChildById(1L));
    }

    @Test
    void shouldDeleteChild() {
        Long id = 1L;

        when(childRepository.findById(id))
                .thenReturn(Optional.of(new Child()));

        childService.deleteById(id);

        verify(childRepository).deleteById(id);
    }

    @Test
    void shouldNotDeleteWhenChildNotExists() {
        when(childRepository.findById(1L))
                .thenReturn(Optional.empty());

        childService.deleteById(1L);

        verify(childRepository, never()).deleteById(any());
    }

    @Test
    void shouldUpdateChild() {
        Long id = 1L;

        Child child = new Child();
        child.setFirstName("Old");

        UpdateChildCommand command = new UpdateChildCommand();
        command.setFirstName("New");

        when(childRepository.findById(id)).thenReturn(Optional.of(child));

        ChildDTO result = childService.updateChild(id, command);

        assertEquals("New", result.getFirstName());
    }

    @Test
    void shouldRemovePresent() {
        childService.removePresent(1L, 2L);

        verify(presentService).deleteById(1L, 2L);
    }
}