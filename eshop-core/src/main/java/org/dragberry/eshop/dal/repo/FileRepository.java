package org.dragberry.eshop.dal.repo;

import java.util.Optional;

import org.dragberry.eshop.dal.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long>{

	Optional<File> findByPath(String path);

}
