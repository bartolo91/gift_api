package org.example.gift_api.repository;

import org.example.gift_api.model.entity.Child;
import org.example.gift_api.model.entity.Present;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChildRepository extends JpaRepository<Child, Long> {

    @Override
    @EntityGraph(value = "Patient.visits", type = EntityGraph.EntityGraphType.FETCH)
    Page<Child> findAll(Pageable page);

    @Query("select presents from Child where id = :id")
    List<Present> findAllPresents(@Param("id") Long id);
}

