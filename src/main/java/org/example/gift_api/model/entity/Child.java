package org.example.gift_api.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * ten NamedEntityGraph jest jeszcze do wywołania przy wyszukiwaniu
 * - zamiast tego można wskazać EntityGraph bezpośrednio na Query
 * */
//@NamedEntityGraph(name = "Child.presentSet",
//        attributeNodes = {@NamedAttributeNode("presents")})
public class Child {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private Long version = 0L;
    private String firstName;
    private String lastName;
    private LocalDate birthDate;

    @OneToMany(mappedBy = "child")
    @Builder.Default
    private Set<Present> presents = new HashSet<>();

}
