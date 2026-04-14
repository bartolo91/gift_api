package org.example.gift_api.controler;

import lombok.RequiredArgsConstructor;
import org.example.gift_api.service.DataGeneratorService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dev")
@RequiredArgsConstructor
public class BatchController {

    private final DataGeneratorService dataGeneratorService;

    @PostMapping("/generate")
    public String generate() {
        dataGeneratorService.generateTestData();
        return "DATA GENERATED";
    }
}
