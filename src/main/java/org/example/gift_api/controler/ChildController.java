package org.example.gift_api.controler;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.gift_api.model.command.CreateChildCommand;
import org.example.gift_api.model.command.CreatePresentCommand;
import org.example.gift_api.model.command.UpdateChildCommand;
import org.example.gift_api.model.command.UpdatePresentCommand;
import org.example.gift_api.model.dto.ChildDTO;
import org.example.gift_api.model.dto.PresentDTO;
import org.example.gift_api.model.entity.ChildView;
import org.example.gift_api.service.ChildService;
import org.example.gift_api.service.PresentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/children")
@RequiredArgsConstructor
public class ChildController {

    private final ChildService childService;
    private final PresentService presentService;

    @PostMapping
    public ResponseEntity<ChildDTO> create(@Valid @RequestBody CreateChildCommand childCommand) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(childService.createChild(childCommand));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ChildDTO> getById(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(childService.getChildById(id));
    }

    @GetMapping
    public ResponseEntity<Page<ChildDTO>> getAll(@PageableDefault Pageable pageable) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(childService.getAll(pageable));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        childService.deleteById(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ChildDTO> update(@PathVariable Long id, @Valid @RequestBody UpdateChildCommand updateCommand) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(childService.updateChild(id, updateCommand));
    }

    @PostMapping("/{childId}/presents")
    public ResponseEntity<PresentDTO> addPresent(@PathVariable Long childId, @Valid @RequestBody CreatePresentCommand presentCommand) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(presentService.create(presentCommand, childId));
    }

    @GetMapping("/{childId}/presents")
    public ResponseEntity<List<PresentDTO>> getChildPresents(@PathVariable Long childId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(childService.findPresentsByChildId(childId));
    }

    @GetMapping("/{childId}/presents/{presentId}")
    public ResponseEntity<PresentDTO> getPresentByChildAndPresentId(@PathVariable Long childId, @PathVariable Long presentId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(childService.getPresentByChildAndPresentId(childId, presentId));
    }

    @DeleteMapping("/{childId}/presents/{presentId}")
    public ResponseEntity<ChildDTO> removePresent(@PathVariable Long childId, @PathVariable Long presentId) {
        childService.removePresent(childId, presentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{childId}/presents/{presentId}")
    public ResponseEntity<PresentDTO> updatePresent(@PathVariable Long childId, @PathVariable Long presentId, @Valid @RequestBody UpdatePresentCommand command) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(childService.updatePresent(childId, presentId, command));
    }

    @GetMapping("/search")
    public List<ChildView> search(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) Integer minPresents,
            @RequestParam(required = false) Integer maxPresents) {
        return childService.findFilteredChildren(name, minAge, maxAge, minPresents, maxPresents);
    }
}

