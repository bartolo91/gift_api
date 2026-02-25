package org.example.gift_api.repository;

import org.example.gift_api.model.entity.Present;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PresentRepository extends JpaRepository<Present, Long> {

}
