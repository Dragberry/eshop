package org.dragberry.eshop.dal.repo;

import org.dragberry.eshop.dal.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Role findByModuleAndAction(String module, String action);

}
