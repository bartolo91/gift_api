package org.example.gift_api.controler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gift_api.model.command.PresentCommand;
import org.example.gift_api.model.dto.PresentDTO;
import org.example.gift_api.model.entity.Present;
import org.example.gift_api.service.PresentService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PresentController {

    private final PresentService presentService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity addPresent(@RequestBody @Valid PresentCommand presentCommand) {
        Present presentEntity = presentService.createPresent(presentCommand);
        PresentDTO presentDTO = modelMapper.map(presentEntity, PresentDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(presentDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PresentDTO> getPresent(@PathVariable Long id) {
        Present findPresent = presentService.getPresentById(id);
        PresentDTO presentDTO = modelMapper.map(findPresent, PresentDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(presentDTO);
    }

    @GetMapping
    public ResponseEntity<List<PresentDTO>> getAllPresents() {
        List<Present> presents = presentService.getAllPresents();
        List<PresentDTO> presentDTOs = presents.stream().map(present -> modelMapper.map(presents, PresentDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(presentDTOs);
    }
}
