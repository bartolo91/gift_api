package org.example.gift_api.service;

import lombok.RequiredArgsConstructor;
import org.example.gift_api.mapper.PresentMapper;
import org.example.gift_api.model.command.CreatePresentCommand;
import org.example.gift_api.model.command.UpdatePresentCommand;
import org.example.gift_api.model.dto.PresentDTO;
import org.example.gift_api.model.entity.Child;
import org.example.gift_api.model.entity.Present;
import org.example.gift_api.repository.ChildRepository;
import org.example.gift_api.repository.PresentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.example.gift_api.exceptions.GiftApiException.notFound;
import static org.example.gift_api.mapper.PresentMapper.mapFromCommand;
import static org.example.gift_api.mapper.PresentMapper.mapToDto;
import static org.example.gift_api.mapper.PresentMapper.updateFromCommand;

@Service
@RequiredArgsConstructor
public class PresentService {

    private final PresentRepository presentRepository;
    private final ChildRepository childRepository;

    @Transactional
    public PresentDTO create(CreatePresentCommand presentCommand, Long childId) {
        Child child = childRepository.findByIdWIthPessimisticLocking(childId)
                .orElseThrow(() -> notFound(Child.class, childId));
        if (child.getPresents().size() >= 3) {
            throw new IllegalArgumentException("Child cant have more than 3 gifts");
        }
        Present present = mapFromCommand(presentCommand);
        present.setChild(child);
        return mapToDto(presentRepository.save(present));
    }

    public PresentDTO findPresentByPresentAndChildId(Long childId, Long presentId) {
        childRepository.findById(childId)
                .orElseThrow(() -> notFound(Child.class, childId));

        return presentRepository.findPresentByPresentAndChildId(childId, presentId)
                .map(PresentMapper::mapToDto)
                .orElseThrow(() -> notFound(Present.class, presentId));
    }

    public List<PresentDTO> findAllByChildId(Long childId) {
        childRepository.findById(childId)
                .orElseThrow(() -> notFound(Child.class, childId));

        return presentRepository.findAllByChildId(childId)
                .stream()
                .map(PresentMapper::mapToDto)
                .toList();
    }

    public void deleteById(Long childId, Long presentId) {
        childRepository.findById(childId)
                .orElseThrow(() -> notFound(Child.class, childId));

        if (presentRepository.findPresentByPresentAndChildId(presentId, childId).isPresent()) {
            presentRepository.deleteById(presentId);
        }
    }

    public PresentDTO update(Long childId, Long presentId, UpdatePresentCommand updateCommand) {
        childRepository.findById(childId)
                .orElseThrow(() -> notFound(Child.class, childId));

        Present present = presentRepository.findPresentByPresentAndChildId(childId, presentId)
                .orElseThrow(() -> notFound(Present.class, childId));

        return PresentMapper.mapToDto(updateFromCommand(present, updateCommand));
    }


}
