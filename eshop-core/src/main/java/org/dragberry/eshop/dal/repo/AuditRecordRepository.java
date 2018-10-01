package org.dragberry.eshop.dal.repo;

import org.dragberry.eshop.dal.entity.AuditRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRecordRepository extends JpaRepository<AuditRecord, Long> {

}