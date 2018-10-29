package org.dragberry.eshop.dal.repo;

import java.util.List;

import org.dragberry.eshop.dal.entity.PaymentMethod;
import org.dragberry.eshop.dal.entity.PaymentMethod.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    List<PaymentMethod> findAllByOrderByOrder();
    
    @Query("select pm from PaymentMethod pm where pm.status in :statuses order by pm.order")
    List<PaymentMethod> findByStatusOrdered(List<Status> statuses);
}
