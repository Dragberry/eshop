package org.dragberry.eshop.dal.repo;

import java.util.List;

import org.dragberry.eshop.dal.entity.DeliveryMethod;
import org.dragberry.eshop.dal.entity.DeliveryMethod.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryMethodRepository extends JpaRepository<DeliveryMethod, Long> {

    List<DeliveryMethod> findByStatusOrderByOrder(Status active);

}
