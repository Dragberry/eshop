package org.dragberry.eshop.dal.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "AUDIT_RECORD")
@TableGenerator(
        name = "AUDIT_RECORD_GEN", 
        table = "GENERATOR",
        pkColumnName = "GEN_NAME", 
        pkColumnValue = "AUDIT_RECORD_GEN",
        valueColumnName = "GEN_VALUE",
        initialValue = 1000,
        allocationSize = 1)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class AuditRecord extends AbstractEntity {

    private static final long serialVersionUID = 86802150774576690L;
    
    @Id
    @Column(name = "AUDIT_RECORD_KEY")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "AUDIT_RECORD_GEN")
    private Long entityKey;
    
    @Column(name = "CREATED_DATE", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;
    
    @Column(name = "METHOD")
    private String method;
    
    @Column(name = "URI")
    private String uri;
    
    @Column(name = "ADDRESS")
    private String address;
    
    @Column(name = "SESSION_ID")
    private String sessionId;
    
    @Column(name = "LOCALE")
    private String locale;
    
    @Column(name = "ENCODING")
    private String encoding;
    
    @Column(name = "PARAMS")
    private String params;

}
