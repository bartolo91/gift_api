package org.example.gift_api.service;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gift_api.exceptions.types.EntityNotFoundException;
import org.example.gift_api.model.command.PresentCommand;
import org.example.gift_api.model.entity.Present;
import org.example.gift_api.repository.PresentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PresentService {

    private final PresentRepository presentRepository;

    public Present createPresent(@Valid PresentCommand presentCommand) {
        return presentRepository.saveAndFlush(Present.builder()
                .name(presentCommand.getName())
                .price(presentCommand.getPrice())
                .build());
    }

    public Present getPresentById(Long id) {
        return presentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Present.class, id));
    }

    public List<Present> getAllPresents() {
        return presentRepository.findAll();
    }
}
