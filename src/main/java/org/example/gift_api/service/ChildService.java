package org.example.gift_api.service;

import lombok.RequiredArgsConstructor;
import org.example.gift_api.exceptions.types.EntityNotFoundException;
import org.example.gift_api.mapper.ChildMapper;
import org.example.gift_api.model.command.CreateChildCommand;
import org.example.gift_api.model.command.CreatePresentCommand;
import org.example.gift_api.model.command.UpdateChildCommand;
import org.example.gift_api.model.command.UpdatePresentCommand;
import org.example.gift_api.model.dto.ChildDTO;
import org.example.gift_api.model.dto.PresentDTO;
import org.example.gift_api.model.entity.Child;
import org.example.gift_api.repository.ChildRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.example.gift_api.mapper.ChildMapper.mapFromCommand;
import static org.example.gift_api.mapper.ChildMapper.mapToDto;
import static org.example.gift_api.mapper.ChildMapper.updateFromCommand;


@Service
@RequiredArgsConstructor
public class ChildService {

    private final ChildRepository childRepository;
    private final PresentService presentService;

    public ChildDTO createChild(CreateChildCommand childCommand) {
        return mapToDto(childRepository.saveAndFlush(mapFromCommand(childCommand)));
        //TODO: wyjaśnić czy save(), czy saveAndFlush() i dlaczego
    }

    @Transactional
    public ChildDTO getChildById(Long id) {
        return childRepository.findById(id)
                .map(ChildMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException(Child.class, id));
    }

    @Transactional
    public Page<ChildDTO> getAllChildren(Pageable pageable) {
  //      return childRepository.findAllWithPresents(pageable)
  //              .map(ChildMapper::mapToDto);
        return childRepository.findAllWithPresentCount(pageable);
        //TODO: jak rozwiązać n+1
    }

    @Transactional
    public Page<ChildDTO> getAllChildrenSortedByPresentsCount(Pageable pageable, boolean asc) {
        if (asc) {
            return childRepository.findAllOrderByPresentsCountAsc(pageable)
                    .map(ChildMapper::mapToDto);
        } else {
            return childRepository.findAllOrderByPresentsCountDesc(pageable)
                    .map(ChildMapper::mapToDto);
        }
    }

    public void deleteById(Long id) {
        if (childRepository.findById(id).isPresent()) {
            childRepository.deleteById(id);
        }
    }

    @Transactional
    public ChildDTO updateChild(Long childId, UpdateChildCommand updateCommand) {
        childRepository.findById(childId)
                .orElseThrow(() -> new EntityNotFoundException(Child.class, childId));

        return mapToDto(childRepository.save(updateFromCommand(updateCommand, childId)));
    }

    @Transactional
    public ChildDTO addPresentByChildId(Long childId, CreatePresentCommand command) {
        Child child = childRepository.findById(childId)
                .orElseThrow(() -> new EntityNotFoundException(Child.class, childId));

        if (child.getPresents().size() >= 3) {
            throw new IllegalArgumentException("Child cant have more than 3 gifts");
        }
        presentService.create(command, child);

        return mapToDto(child);
    }

    public List<PresentDTO> findPresentsByChildId(Long childId) {
        childRepository.findById(childId)
                .orElseThrow(() -> new EntityNotFoundException(Child.class, childId));

        return presentService.findAllByChildId(childId);
    }

    public void removePresent(Long childId, Long presentId) {
        childRepository.findById(childId)
                .orElseThrow(() -> new EntityNotFoundException(Child.class, childId));
        presentService.findPresentByPresentAndChildId(presentId, childId);
        presentService.deleteById(presentId);
    }

    public PresentDTO getPresentByChildAndPresentId(Long childId, Long presentId) {
        childRepository.findById(childId)
                .orElseThrow(() -> new EntityNotFoundException(Child.class, childId));

        return presentService.findPresentByPresentAndChildId(presentId, childId);
    }

    @Transactional
    public PresentDTO updatePresent(Long childId, Long presentId, UpdatePresentCommand updateCommand) {
        childRepository.findById(childId)
                .orElseThrow(() -> new EntityNotFoundException(Child.class, childId));

        return presentService.update(childId, presentId, updateCommand);
    }
}
