package org.example.gift_api.controler;

import lombok.RequiredArgsConstructor;
import org.example.gift_api.service.DataLoadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/batch")
public class DataLoadController {

    private final DataLoadService dataLoadService;

    @PostMapping("/children")
    public ResponseEntity<Void> saveChildren() {
        dataLoadService.saveChildren();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/presents")
    public ResponseEntity<Void> savePresents() {
        dataLoadService.savePresents();
        return ResponseEntity.ok().build();
    }
}
