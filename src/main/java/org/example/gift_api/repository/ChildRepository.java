package org.example.gift_api.repository;

import jakarta.persistence.LockModeType;
import org.example.gift_api.model.dto.ChildDTO;
import org.example.gift_api.model.entity.Child;
import org.example.gift_api.model.entity.ChildView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {

    /**
     * EntityGraph i uzyanie join fetch przy paginacji powoduje wykonaywanie paginacji w pamięci - co powoduje pojawienie się warningu
     **/
    //   @EntityGraph(attributePaths = "presents")
    //   @Query("select c from Child c")
    //Page<Child> findAllWithPresents(Pageable pageable);
    @Query(
            value = """
                        SELECT new org.example.gift_api.model.dto.ChildDTO(
                            c.id,
                            c.firstName,
                            c.lastName,
                            c.birthDate,
                            COUNT(p) AS presentsCount,
                            c.version
                        )
                        FROM Child c
                        LEFT JOIN c.presents p
                        GROUP BY c
                    """,
            countQuery = "SELECT COUNT(c) FROM Child c"
    )
    Page<ChildDTO> findAllWithPresentCount(Pageable pageable);

    @Query(value = "select distinct c from Child  c left join fetch c.presents where c.id = :id",
            countQuery = "select count(c) from Child c")
    Optional<Child> findByIdWithPresentCount(Long id);

    @Query("""
                SELECT c
                FROM ChildView c
                WHERE (:name IS NULL OR LOWER(c.firstName) LIKE LOWER(CONCAT('%', :name, '%')))
                  AND (:minAge IS NULL OR c.age >= :minAge)
                  AND (:maxAge IS NULL OR c.age <= :maxAge)
                  AND (:minPresents IS NULL OR c.presentsCount >= :minPresents)
                  AND (:maxPresents IS NULL OR c.presentsCount <= :maxPresents)
            """)
    List<ChildView> findFilteredChildren(
            @Param("name") String name,
            @Param("minAge") Integer minAge,
            @Param("maxAge") Integer maxAge,
            @Param("minPresents") Integer minPresents,
            @Param("maxPresents") Integer maxPresents
    );

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select c from Child c where c.id = :id")
    Optional<Child> findByIdWIthPessimisticLocking(Long id);

}

