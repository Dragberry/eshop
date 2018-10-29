package org.dragberry.eshop.dal.repo;

import java.util.List;

import org.dragberry.eshop.dal.entity.ShippingMethod;
import org.dragberry.eshop.dal.entity.ShippingMethod.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingMethodRepository extends JpaRepository<ShippingMethod, Long> {

    List<ShippingMethod> findAllByOrderByOrder();
    
    @Query("select sm from ShippingMethod sm where sm.status in :statuses order by sm.order")
    List<ShippingMethod> findByStatusOrdered(List<Status> statuses);

}
