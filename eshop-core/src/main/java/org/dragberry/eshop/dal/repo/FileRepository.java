package org.dragberry.eshop.dal.repo;

import org.dragberry.eshop.dal.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long>{

}
