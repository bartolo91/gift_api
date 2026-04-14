package org.example.gift_api.repository;

import org.example.gift_api.model.entity.ChildView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChildViewRepository extends JpaRepository<ChildView, Long>, JpaSpecificationExecutor<ChildView> {

}
