package org.example.gift_api.service;

import lombok.RequiredArgsConstructor;
import org.example.gift_api.model.entity.Child;
import org.example.gift_api.model.entity.Present;
import org.example.gift_api.repository.ChildRepository;
import org.example.gift_api.repository.PresentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class DataGeneratorService {

    private final ChildRepository childRepository;
    private final PresentRepository presentRepository;

    @Transactional
    public void generateTestData() {

        for (int i = 1; i <= 10_000; i++) {

            Child child = new Child();
            child.setFirstName("Child_" + i);
            child.setLastName("Test");
            child.setBirthDate(LocalDate.of(2010, 1, 1));

            Child savedChild = childRepository.save(child);

            int presentsCount = ThreadLocalRandom.current().nextInt(2, 4);

            List<Present> presents = new ArrayList<>();

            for (int j = 0; j < presentsCount; j++) {
                Present present = new Present();
                present.setName("Present_" + i + "_" + j);

                boolean expensive = ThreadLocalRandom.current().nextBoolean();
                present.setPrice(expensive ? BigDecimal.valueOf(150) : BigDecimal.valueOf(50));

                present.setChild(savedChild);
                presents.add(present);
            }

            presentRepository.saveAll(presents);
        }
    }
}