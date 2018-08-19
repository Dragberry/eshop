package org.dragberry.eshop.dal.repo;

import org.dragberry.eshop.dal.entity.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends CrudRepository<UserAccount, Long> {

	UserAccount findByUsername(String username);

}
