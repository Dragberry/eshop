package org.dragberry.eshop.dal.entity;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.dragberry.eshop.dal.entity.converter.DeviceConverter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "REQUEST_LOG")
@TableGenerator(
        name = "REQUEST_LOG_GEN", 
        table = "GENERATOR",
        pkColumnName = "GEN_NAME", 
        pkColumnValue = "REQUEST_LOG_GEN",
        valueColumnName = "GEN_VALUE",
        initialValue = 1000,
        allocationSize = 1)
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public class RequestLog extends AbstractEntity {

    private static final long serialVersionUID = 86802150774576690L;
    
    @Id
    @Column(name = "REQUEST_LOG_KEY")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "REQUEST_LOG_GEN")
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
    
    @Convert(converter = DeviceConverter.class)
	@Column(name = "DEVICE")
    private Device device;
    
    @Column(name = "SESSION_ID")
    private String sessionId;
    
    @Column(name = "LOCALE")
    private String locale;
    
    @Column(name = "ENCODING")
    private String encoding;
    
    @Column(name = "HEADERS")
    private String headers;
    
    @OneToOne(cascade = CascadeType.ALL, mappedBy = "requestLog", fetch = FetchType.LAZY)
    private RequestLogData data;
    
    public static enum Device implements BaseEnum<Character> {

		NORMAL('N'), MOBILE('M'), TABLET('T'), UNKNOWN('U');
	    
	    public final Character value;
	    
	    private Device(Character value) {
	        this.value = value;
	    }
	    
	    public static Device valueOf(org.springframework.mobile.device.Device device) {
	    	if (device.isNormal()) {
	    		return Device.NORMAL;
	    	}
	    	if (device.isMobile()) {
	    		return Device.MOBILE;
	    	}
	    	if (device.isTablet()) {
	    		return Device.TABLET;
	    	}
	    	return UNKNOWN;
	    }
	    
	    public static Device valueOf(Character value) {
	        if (value == null) {
	            throw BaseEnum.npeException(Device.class);
	        }
	        for (Device status : Device.values()) {
	            if (value.equals(status.value)) {
	                return status;
	            }
	        }
	        throw BaseEnum.unknownValueException(Device.class, value);
	    }
	    
	    @Override
	    public Character getValue() {
	        return value;
	    }
	}
    
}
