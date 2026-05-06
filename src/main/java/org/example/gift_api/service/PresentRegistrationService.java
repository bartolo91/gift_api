package org.example.gift_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gift_api.model.dto.ChildPresentProcessingDTO;
import org.example.gift_api.repository.ChildRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PresentRegistrationService {

    private final ChildRepository childRepository;
    private final EmailService emailService;

    @Value("${present.processing.page-size}")
    private int pageSize;

    @Value("${present.processing.limit-price}")
    private BigDecimal limitPrice;

    @Transactional(readOnly = true)
    public void process() {

        int pageNumber = 0;

        Page<ChildPresentProcessingDTO> page;

        do {
            Pageable pageable = PageRequest.of(pageNumber, pageSize);
            page = childRepository.findChildrenWithExpensivePresents(limitPrice, pageable);

            List<ChildPresentProcessingDTO> batch = page.getContent();

            log.info("Page {}: {} children", pageNumber, batch.size());

            emailService.processBatch(batch);
            pageNumber++;

        } while (page.hasNext());
    }
}