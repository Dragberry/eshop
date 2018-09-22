package org.dragberry.eshop.dal.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class AuditableEntity extends BaseEntity {

	private static final long serialVersionUID = -8952555002443715114L;

	@Column(name = "CREATED_DATE", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;
 
    @Column(name = "MODIFIED_DATE")
    @LastModifiedDate
    private LocalDateTime modifiedDate;
}
