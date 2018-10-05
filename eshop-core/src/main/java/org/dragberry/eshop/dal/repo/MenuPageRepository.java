package org.dragberry.eshop.dal.repo;

import java.util.List;

import org.dragberry.eshop.dal.dto.MenuPageDTO;
import org.dragberry.eshop.dal.entity.MenuPage;
import org.dragberry.eshop.dal.entity.MenuPage.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuPageRepository extends JpaRepository<MenuPage, Long> {
    
    @Query("select new org.dragberry.eshop.dal.dto.MenuPageDTO(p.reference, p.title) from MenuPage mp join mp.page p where mp.status = :status order by mp.order")
    List<MenuPageDTO> getMenu(Status status);
}
