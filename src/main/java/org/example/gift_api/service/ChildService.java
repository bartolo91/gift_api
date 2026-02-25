package org.example.gift_api.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gift_api.exceptions.types.EntityNotFoundException;
import org.example.gift_api.model.command.CreateChildCommand;
import org.example.gift_api.model.entity.Child;
import org.example.gift_api.model.entity.Present;
import org.example.gift_api.repository.ChildRepository;
import org.example.gift_api.repository.PresentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.example.gift_api.constants.Constants.PAGE_SIZE;

@Service
@RequiredArgsConstructor
public class ChildService {

    private final ChildRepository childRepository;
    private final PresentRepository presentRepository;

    public Child createChild(@Valid CreateChildCommand childCommand) {
        return childRepository.saveAndFlush(Child.builder()
                .firstName(childCommand.getFirstName())
                .lastName(childCommand.getLastName())
                .birthDate(childCommand.getBirthDate())
                .build());
    }

    public Child getChildById(Long id) {
        return childRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Child.class, id));
    }

    public Page<Child> getAllChildren(int page) {
        return childRepository.findAll(PageRequest.of(page, PAGE_SIZE));
    }

    public void deleteById(Long id) {
        childRepository.deleteById(id);
    }

    public List<Present> getAllPresents(Long id) {
        return childRepository.findAllPresents(id);
    }

    public Present addPresent(Long childId, CreatePresentCommand command) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new EntityNotFoundException(Child.class, childId));
        //weryfikacja liczby prezentów

        //mapowanie prezentu do encji

        present.setChild(child);
        return PresentMapper.mapToDto(presentRepository.save(present));
    }




    public Child removePresent(Long childId, Long presentId) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new EntityNotFoundException(Child.class, childId));
        Present present = presentRepository.findById(presentId)
                .orElseThrow(() -> new EntityNotFoundException(Present.class, presentId));
        child.removePresent(present);
        return childRepository.save(child);
    }

}
