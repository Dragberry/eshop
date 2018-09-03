package org.dragberry.eshop.dal.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.dragberry.eshop.dal.entity.Product.SaleStatus;
import org.dragberry.eshop.dal.entity.converter.DeliveryMethodStatusConverter;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "DELIVERY_METHOD")
@TableGenerator(
		name = "DELIVERY_METHOD_GEN", 
		table = "GENERATOR",
		pkColumnName = "GEN_NAME", 
		pkColumnValue = "DELIVERY_METHOD_GEN",
		valueColumnName = "GEN_VALUE",
		initialValue = 1000,
		allocationSize = 1)
@Setter
@Getter
public class DeliveryMethod extends BaseEntity {

	private static final long serialVersionUID = 6817451222642163283L;

	@Id
	@Column(name = "DELIVERY_METHOD_KEY")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "DELIVERY_METHOD_GEN")
	private Long entityKey;
	
	@Column(name = "NAME")
    private String name;
	
	@Column(name = "DESCRIPTION")
    private String description;
	
	@Column(name = "PRICE")
    private BigDecimal price;
	
	@Convert(converter = DeliveryMethodStatusConverter.class)
	@Column(name = "STATUS")
    private Status status;
	
	public static enum Status implements BaseEnum<Character> {

		ACTIVE('A'), INACTIVE('I');
	    
	    public final Character value;
	    
	    private Status(Character value) {
	        this.value = value;
	    }
	    
	    public static Status valueOf(Character value) {
	        if (value == null) {
	            throw BaseEnum.npeException(SaleStatus.class);
	        }
	        for (Status status : Status.values()) {
	            if (value.equals(status.value)) {
	                return status;
	            }
	        }
	        throw BaseEnum.unknownValueException(Status.class, value);
	    }
	    
	    @Override
	    public Character getValue() {
	        return value;
	    }
	}
	
}
