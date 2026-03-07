package org.example.gift_api.service;

import lombok.RequiredArgsConstructor;
import org.example.gift_api.exceptions.types.EntityNotFoundException;
import org.example.gift_api.mapper.PresentMapper;
import org.example.gift_api.model.command.CreatePresentCommand;
import org.example.gift_api.model.command.UpdatePresentCommand;
import org.example.gift_api.model.dto.PresentDTO;
import org.example.gift_api.model.entity.Child;
import org.example.gift_api.model.entity.Present;
import org.example.gift_api.repository.PresentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.gift_api.mapper.PresentMapper.updateFromCommand;

@Service
@RequiredArgsConstructor
public class PresentService {

    private final PresentRepository presentRepository;

    public Present create(CreatePresentCommand presentCommand, Child child) {
        return presentRepository.saveAndFlush(Present.builder()
                .name(presentCommand.getName())
                .price(presentCommand.getPrice())
                .child(child)
                .build());
    }

    public PresentDTO findPresentByPresentAndChildId(Long childId, Long presentId) {
        return presentRepository.findPresentByPresentAndChildId(childId, presentId)
                .map(PresentMapper::mapToDto)
                .orElseThrow(() -> new EntityNotFoundException(Present.class, presentId));
    }

    public List<PresentDTO> findAllByChildId(Long childId) {
        return presentRepository.findAllByChildId(childId)
                .stream()
                .map(PresentMapper::mapToDto).collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        if (presentRepository.findById(id).isPresent()) {
            presentRepository.deleteById(id);
        }
    }

    public PresentDTO update(Long childId, Long presentId, UpdatePresentCommand updateCommand) {
        Present present = presentRepository.findPresentByPresentAndChildId(childId, presentId)
                .orElseThrow(() -> new EntityNotFoundException(Present.class, presentId));

        return PresentMapper.mapToDto(updateFromCommand(present, updateCommand));
    }
}
