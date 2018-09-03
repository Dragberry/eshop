package org.dragberry.eshop.dal.entity;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.dragberry.eshop.dal.entity.Product.SaleStatus;
import org.dragberry.eshop.dal.entity.converter.PaymentMethodStatusConverter;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "PAYMENT_METHOD")
@TableGenerator(
		name = "PAYMENT_METHOD_GEN", 
		table = "GENERATOR",
		pkColumnName = "GEN_NAME", 
		pkColumnValue = "PAYMENT_METHOD_GEN",
		valueColumnName = "GEN_VALUE",
		initialValue = 1000,
		allocationSize = 1)
@Setter
@Getter
public class PaymentMethod extends BaseEntity {

	private static final long serialVersionUID = 6817451222642163283L;

	@Id
	@Column(name = "PAYMENT_METHOD_KEY")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "PAYMENT_METHOD_GEN")
	private Long entityKey;
	
	@Column(name = "NAME")
    private String name;
	
	@Column(name = "DESCRIPTION")
    private String description;
	
	@Convert(converter = PaymentMethodStatusConverter.class)
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
