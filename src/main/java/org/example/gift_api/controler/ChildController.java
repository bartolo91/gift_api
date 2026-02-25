package org.example.gift_api.controler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gift_api.model.command.CreateChildCommand;
import org.example.gift_api.model.dto.ChildDTO;
import org.example.gift_api.model.dto.PresentDTO;
import org.example.gift_api.model.entity.Child;
import org.example.gift_api.model.entity.Present;
import org.example.gift_api.service.ChildService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/children")
@RequiredArgsConstructor
public class ChildController {

    private final ChildService childService;
    private final ModelMapper modelMapper;

    @PostMapping
    public ResponseEntity<ChildDTO> addChild(@RequestBody @Valid CreateChildCommand childCommand) {
        Child childEntity = childService.createChild(childCommand);
        ChildDTO childDTO = modelMapper.map(childEntity, ChildDTO.class);
        return ResponseEntity.status(HttpStatus.CREATED).body(childDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChildDTO> getChild(@PathVariable Long id) {
        Child findChild = childService.getChildById(id);
        ChildDTO childDTO = modelMapper.map(findChild, ChildDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(childDTO);
    }

    @GetMapping
    public ResponseEntity<List<ChildDTO>> getAllChildren(@RequestParam(required = false, defaultValue = "0") int page) {
        int pageNumber = page < 0 ? 0 : page;
       Page<Child> children = childService.getAllChildren(pageNumber);
        List<ChildDTO> childDTOs = children
                .stream()
                .map(child -> modelMapper.map(child, ChildDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(childDTOs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChild(@PathVariable Long id) {
        Child findChild = childService.getChildById(id);
        if (findChild != null) {
            childService.deleteById(id);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/presents")
    public ResponseEntity<List<PresentDTO>> getChildPresents(@PathVariable Long id) {
        List<Present> presents = childService.getAllPresents(id);
        List<PresentDTO> presentsDTOs = presents
                .stream()
                .map(present -> modelMapper.map(present, PresentDTO.class))
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(presentsDTOs);
    }

    @PostMapping("/{id}/presents/")
    public ResponseEntity<ChildDTO> addPresent(@PathVariable Long childId) {
        Child child = childService.addPresent(childId, presentId);
        ChildDTO childDTO = modelMapper.map(child, ChildDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(childDTO);
    }





    @DeleteMapping("/{id}/presents/{presentId}")
    public ResponseEntity<ChildDTO> removePresent(@PathVariable Long childId, @PathVariable Long presentId) {
        Child child = childService.removePresent(childId, presentId);
        ChildDTO childDTO = modelMapper.map(child, ChildDTO.class);
        return ResponseEntity.status(HttpStatus.OK).body(childDTO);
    }

}

