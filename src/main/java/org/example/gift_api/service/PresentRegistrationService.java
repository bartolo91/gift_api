package org.example.gift_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gift_api.model.entity.Present;
import org.example.gift_api.repository.PresentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PresentRegistrationService {

    private final PresentRepository presentRepository;
    private static final int PAGE_SIZE = 1000;

    @Transactional
    public void process() {
        log.info("Processing...");

        BigDecimal limitPrice = BigDecimal.valueOf(100);
        int pageNumber = 0;

        while (true) {
            Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);
            Page<Present> page = presentRepository.findByPriceGreaterThan(limitPrice, pageable);

            if (page.isEmpty()) {
                break;
            }

            processPage(page.getContent());

            if (!page.hasNext()) {
                break;
            }
            pageNumber++;

            log.info("Processing finished.");
        }
    }

    private void processPage(List<Present> presents) {
        Map<Long, List<Present>> grouped = presents.stream()
                .collect(Collectors.groupingBy(p -> p.getChild().getId()));

        grouped.forEach(this::processChild);
    }

    private void processChild(Long childId, List<Present> presents) {
        log.info("Child {} has {} presents > 100 PLN", childId, presents.size());

        for (Present p : presents) {
            log.info("Present: {} price: {}", p.getName(), p.getPrice());
        }
    }
}
