package org.dragberry.eshop.dal.repo;

import org.dragberry.eshop.dal.entity.Role;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role, Long> {
	
	Role findByModuleAndAction(String module, String action);

}
