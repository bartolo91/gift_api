package org.example.gift_api.service;

import lombok.extern.slf4j.Slf4j;
import org.example.gift_api.model.dto.ChildPresentProcessingDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class EmailService {

    @Async("asyncTaskExecutor")
    public void processBatch(List<ChildPresentProcessingDTO> batch) {
        for (ChildPresentProcessingDTO dto : batch) {
            log.info("Sending email to child: {} {} (id={})", dto.getFirstName(), dto.getLastName(), dto.getId());
            log.info("Count of presensts with price >100 PLN: {}", dto.getPresentsCount());
        }
    }
}
