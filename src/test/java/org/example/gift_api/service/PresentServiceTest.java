package org.example.gift_api.service;

import org.example.gift_api.exceptions.GiftApiException;
import org.example.gift_api.model.command.CreatePresentCommand;
import org.example.gift_api.model.command.UpdatePresentCommand;
import org.example.gift_api.model.dto.PresentDTO;
import org.example.gift_api.model.entity.Child;
import org.example.gift_api.model.entity.Present;
import org.example.gift_api.repository.ChildRepository;
import org.example.gift_api.repository.PresentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PresentServiceTest {

    @Mock
    private PresentRepository presentRepository;

    @Mock
    private ChildRepository childRepository;

    @InjectMocks
    private PresentService presentService;

    @Test
    void shouldCreatePresent() {
        Long childId = 1L;

        Child child = new Child();
        child.setId(childId);
        child.setPresents(new HashSet<>());

        CreatePresentCommand command = new CreatePresentCommand();
        command.setName("Lego");
        command.setPrice(BigDecimal.valueOf(100));

        when(childRepository.findWithLockingById(childId))
                .thenReturn(Optional.of(child));

        when(presentRepository.save(any()))
                .thenAnswer(inv -> inv.getArgument(0));

        PresentDTO result = presentService.create(command, childId);

        assertEquals("Lego", result.getName());

        verify(presentRepository).save(any());
    }

    @Test
    void shouldThrowExceptionWhenChildHasMoreThan3Presents() {
        Long childId = 1L;

        Child child = new Child();
        child.setPresents(Set.of(new Present(), new Present(), new Present()));

        when(childRepository.findWithLockingById(childId))
                .thenReturn(Optional.of(child));

        CreatePresentCommand command = new CreatePresentCommand();

        assertThrows(IllegalArgumentException.class, () ->
                presentService.create(command, childId)
        );
    }

    @Test
    void shouldFindPresentByChildAndPresentId() {
        Long childId = 1L;
        Long presentId = 2L;

        when(childRepository.findById(childId))
                .thenReturn(Optional.of(new Child()));

        Present present = new Present();
        present.setId(presentId);

        when(presentRepository.findPresentByPresentAndChildId(childId, presentId))
                .thenReturn(Optional.of(present));

        PresentDTO presentDTO = presentService.findPresentByPresentAndChildId(childId, presentId);

        assertEquals(presentId, presentDTO.getId());
    }

    @Test
    void shouldFindAllPresentsByChildId() {
        Long childId = 1L;

        when(childRepository.findById(childId))
                .thenReturn(Optional.of(new Child()));

        when(presentRepository.findAllByChildId(childId))
                .thenReturn(List.of(new Present(), new Present()));

        List<PresentDTO> presents = presentService.findAllByChildId(childId);

        assertEquals(2, presents.size());
    }

    @Test
    void shouldDeletePresent() {
        Long childId = 1L;
        Long presentId = 2L;

        Child child = new Child();
        Present present = new Present();
        present.setId(presentId);

        when(childRepository.findById(childId)).thenReturn(Optional.of(child));

        when(presentRepository.findPresentByPresentAndChildId(presentId, childId))
                .thenReturn(Optional.of(present));

        presentService.deleteById(childId, presentId);

        verify(presentRepository, times(1)).deleteById(presentId);
    }

    @Test
    void shouldUpdatePresent() {
        Long childId = 1L;
        Long presentId = 2L;

        when(childRepository.findById(childId))
                .thenReturn(Optional.of(new Child()));

        Present present = new Present();
        present.setName("Stary prezent");

        when(presentRepository.findPresentByPresentAndChildId(childId, presentId))
                .thenReturn(Optional.of(present));

        UpdatePresentCommand updatedPresentCommand = new UpdatePresentCommand();
        updatedPresentCommand.setName("Nowy prezent");

        PresentDTO updatedPresentDTO = presentService.update(childId, presentId, updatedPresentCommand);

        assertEquals("Nowy prezent", updatedPresentDTO.getName());
    }

    @Test
    void shouldThrowIfChildDoesNotExist() {
        Long childId = 1L;
        Long presentId = 2L;

        when(childRepository.findById(childId)).thenReturn(Optional.empty());

        assertThrows(GiftApiException.class,
                () -> presentService.deleteById(childId, presentId));
    }
}