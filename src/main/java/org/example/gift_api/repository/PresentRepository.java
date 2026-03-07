package org.example.gift_api.repository;

import org.example.gift_api.model.entity.Present;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PresentRepository extends JpaRepository<Present, Long> {

    @Query("SELECT p FROM Present p WHERE p.child.id = :childId")
    List<Present> findAllByChildId(@Param("childId") Long childId);

    @Query("SELECT p FROM Present p WHERE p.id = :presentId AND p.child.id = :childId")
    Optional<Present> findPresentByPresentAndChildId(Long childId, Long presentId);
}