package org.example.gift_api.service;

import lombok.RequiredArgsConstructor;
import org.example.gift_api.mapper.ChildMapper;
import org.example.gift_api.model.ChildSpecification;
import org.example.gift_api.model.command.CreateChildCommand;
import org.example.gift_api.model.command.UpdateChildCommand;
import org.example.gift_api.model.command.UpdatePresentCommand;
import org.example.gift_api.model.dto.ChildDTO;
import org.example.gift_api.model.dto.PresentDTO;
import org.example.gift_api.model.entity.Child;
import org.example.gift_api.model.entity.ChildView;
import org.example.gift_api.repository.ChildRepository;
import org.example.gift_api.repository.ChildViewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.example.gift_api.exceptions.GiftApiException.badRequest;
import static org.example.gift_api.exceptions.GiftApiException.notFound;
import static org.example.gift_api.mapper.ChildMapper.mapFromCommand;
import static org.example.gift_api.mapper.ChildMapper.mapToDto;
import static org.example.gift_api.mapper.ChildMapper.updateFromCommand;


@Service
@RequiredArgsConstructor
public class ChildService {

    private final ChildRepository childRepository;
    private final PresentService presentService;
    private final ChildViewRepository childViewRepository;

    public ChildDTO createChild(CreateChildCommand childCommand) {
        return mapToDto(childRepository.save(mapFromCommand(childCommand)));
    }

    public ChildDTO getChildById(Long id) {
        return childRepository.findByIdWithPresentCount(id)
                .map(ChildMapper::mapToDto)
                .orElseThrow(() -> notFound(Child.class, id));
    }

    public Page<ChildDTO> getAll(Pageable pageable) {
        return childRepository.findAllWithPresentCount(pageable);
    }

    public void deleteById(Long id) {
        if (childRepository.findById(id).isPresent()) {
            childRepository.deleteById(id);
        }
    }

    @Transactional
    public ChildDTO updateChild(Long id, UpdateChildCommand updateCommand) {
        childRepository.findById(id)
                .orElseThrow(() -> notFound(Child.class, id));
        try {
            Child updated = updateFromCommand(id, updateCommand);
            return mapToDto(childRepository.save(updated));
        } catch (ObjectOptimisticLockingFailureException e) {
            throw badRequest();
        }
    }

    public List<PresentDTO> findPresentsByChildId(Long childId) {
        return presentService.findAllByChildId(childId);
    }

    public void removePresent(Long childId, Long presentId) {
        presentService.deleteById(childId, presentId);
    }

    public PresentDTO getPresentByChildAndPresentId(Long childId, Long presentId) {
        return presentService.findPresentByPresentAndChildId(presentId, childId);
    }

    @Transactional
    public PresentDTO updatePresent(Long childId, Long presentId, UpdatePresentCommand updateCommand) {
        return presentService.update(childId, presentId, updateCommand);
    }

    public List<ChildView> findFilteredChildren(String name, Integer minAge, Integer maxAge, Integer minPresents, Integer maxPresents) {
        return childRepository.findFilteredChildren(name, minAge, maxAge, minPresents, maxPresents);
    }

    public Page<ChildView> search(Pageable pageable, String firstName, String lastName, Integer age, Integer presents) {
        Specification<ChildView> spec = ChildSpecification.hasFirstName(firstName)
                .and(ChildSpecification.hasLastName(lastName))
                .and(ChildSpecification.hasMinAge(age))
                .and(ChildSpecification.hasMinPresents(presents));

        return childViewRepository.findAll(spec, pageable);
    }
}
