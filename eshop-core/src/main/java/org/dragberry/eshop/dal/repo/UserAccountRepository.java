package org.dragberry.eshop.dal.repo;

import org.dragberry.eshop.dal.entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {

	UserAccount findByUsername(String username);

}
