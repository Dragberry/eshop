package org.dragberry.eshop.dal.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.apache.commons.lang3.StringUtils;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "REQUEST_LOG_DATA")
@Getter
@Setter
public class RequestLogData extends AbstractEntity {

    private static final long serialVersionUID = 86802150774576690L;
    
    @Id
    private Long entityKey;
    
    @OneToOne
    @MapsId
    @JoinColumn(name = "REQUEST_LOG_KEY", referencedColumnName = "REQUEST_LOG_KEY")
    private RequestLog requestLog;
    
    @Column(name = "QUERY_STRING")
    private String queryString;
    
    @Column(name = "BODY")
    private String body;

    public boolean isEmpty() {
        return StringUtils.isBlank(queryString) && StringUtils.isBlank(body) ;
    }

}
