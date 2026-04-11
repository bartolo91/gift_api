package org.example.gift_api.model;

import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.example.gift_api.model.entity.ChildView;
import org.example.gift_api.model.entity.Present;
import org.springframework.data.jpa.domain.Specification;

public class ChildSpecification {

    public static Specification<ChildView> hasFirstName(String firstName) {
        return (root, query, cb) ->
                firstName == null ? null :
                        cb.like(cb.lower(root.get("firstName")), "%" + firstName.toLowerCase() + "%");
    }

    public static Specification<ChildView> hasLastName(String lastName) {
        return (root, query, cb) ->
                lastName == null ? null :
                        cb.like(cb.lower(root.get("firstName")), "%" + lastName.toLowerCase() + "%");
    }

    public static Specification<ChildView> hasMinAge(Integer age) {
        return (root, query, cb) ->
                age == null ? null :
                        cb.greaterThanOrEqualTo(root.get("age"), age);
    }

    public static Specification<ChildView> hasMinPresents(Integer count) {
        return (root, query, cb) ->
                count == null ? null :
                        cb.greaterThanOrEqualTo(root.get("presentsCount"), count);
    }
}