package org.example.gift_api.repository;

import org.example.gift_api.mapper.ChildMapper;
import org.example.gift_api.model.dto.ChildDTO;
import org.example.gift_api.model.entity.Child;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChildRepository extends JpaRepository<Child, Long> {

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
                            COUNT(p)
                        )
                        FROM Child c
                        LEFT JOIN c.presents p
                        GROUP BY c
                    """,
            countQuery = "SELECT COUNT(c) FROM Child c"
    )
    Page<ChildDTO> findAllWithPresentCount(Pageable pageable);

    @Query(value = "select c from Child c join c.presents ad group by c Order By size(c.presents) asc",
            countQuery = "select count(c) from Child c")
    Page<Child> findAllOrderByPresentsCountAsc(Pageable pageable);

    @Query(value = "select c from Child c join c.presents ad group by c Order By size(c.presents) desc",
            countQuery = "select count(c) from Child c")
    Page<Child> findAllOrderByPresentsCountDesc(Pageable pageable);

}

