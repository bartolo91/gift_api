package org.example.gift_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class PresentLoader {

    private final JdbcTemplate jdbcTemplate;

    private static final int TOTAL_CHILDREN = 2_000_000;
    private static final int BATCH_SIZE = 10_000;

    public void loadPresents() {

        StringBuilder sql = new StringBuilder();
        int counter = 0;
        int global = 0;

        for (long childId = 1; childId <= TOTAL_CHILDREN; childId++) {

            int presentCount = 3;

            for (int i = 0; i < presentCount; i++) {

                if (counter == 0) {
                    sql.setLength(0);
                    sql.append("INSERT INTO present (child_id, name, price) VALUES ");
                } else {
                    sql.append(",");
                }

                boolean expensive = i < 2;

                sql.append("(")
                        .append(childId).append(",")
                        .append("'P").append(childId).append("_").append(i).append("',")
                        .append(expensive ? 150 : 50)
                        .append(")");

                counter++;
                global++;

                if (global % 10_000 == 0) {
                    log.info("PRESENT GENERATED: {}", global);
                }

                if (counter == BATCH_SIZE) {
                    jdbcTemplate.execute(sql.toString());
                    log.info("PRESENT FLUSHED batch at childId={}", childId);
                    counter = 0;
                }
            }
        }

        if (counter > 0) {
            jdbcTemplate.execute(sql.toString());
        }
    }
}