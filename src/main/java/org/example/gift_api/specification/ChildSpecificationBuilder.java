package org.example.gift_api.specification;

import org.example.gift_api.model.entity.ChildView;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class ChildSpecificationBuilder {

    private final List<SearchCriteria> params = new ArrayList<>();

    public ChildSpecificationBuilder with(String key, String operation, Object value, boolean orPredicate) {
        params.add(new SearchCriteria(key, operation, value, orPredicate));
        return this;
    }

    public Specification<ChildView> build() {
        if (params.isEmpty()) {
            return null;
        }

        Specification<ChildView> result = new ChildSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            SearchCriteria criteria = params.get(i);

            result = criteria.isOrPredicate()
                    ? result.or(new ChildSpecification(criteria))
                    : result.and(new ChildSpecification(criteria));
        }

        return result;
    }
}