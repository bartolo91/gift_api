package org.example.gift_api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class ChildLoader {

    private final JdbcTemplate jdbcTemplate;

    private static final int TOTAL = 2_000_000;
    private static final int BATCH = 5_000;

    public void loadChildren() {

        StringBuilder sql = new StringBuilder();
        int counter = 0;

        for (int i = 1; i <= TOTAL; i++) {

            if (counter == 0) {
                sql.setLength(0);
                sql.append("INSERT INTO child (first_name, last_name, birth_date, email) VALUES ");
            } else {
                sql.append(",");
            }

            sql.append("('Jan").append(i).append("','Nowak").append(i).append("','2010-01-01','mail").append(i).append("@test.com')");

            counter++;

            if (i % 10_000 == 0) {
                log.info("CHILD PREPARED: {}", i);
            }

            if (counter == BATCH) {
                jdbcTemplate.execute(sql.toString());
                log.info("CHILD FLUSHED: {}", i);
                counter = 0;
            }
        }

        if (counter > 0) {
            jdbcTemplate.execute(sql.toString());
        }
    }
}