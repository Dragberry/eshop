package org.dragberry.eshop.dal.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import org.dragberry.eshop.dal.entity.converter.OrderStatusConverter;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "`ORDER`")
@TableGenerator(
		name = "ORDER_GEN", 
		table = "GENERATOR",
		pkColumnName = "GEN_NAME", 
		pkColumnValue = "ORDER_GEN",
		valueColumnName = "GEN_VALUE",
		initialValue = 1000,
		allocationSize = 1)
@Setter
@Getter
public class Order extends AuditableEntity {

	private static final long serialVersionUID = 6817451222642163283L;

	@Id
	@Column(name = "ORDER_KEY")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ORDER_GEN")
	private Long entityKey;
	
	@Column(name = "FULL_NAME")
    private String fullName;
	
	@Column(name = "PHONE")
    private String phone;
	
	@Column(name = "ADDRESS")
    private String address;
	
	@Column(name = "EMAIL")
    private String email;
	
	@Column(name = "COMMENT")
    private String comment;
	
	@Column(name = "TOTAL_PRODUCT_AMOUNT")
    private BigDecimal totalProductAmount;
	
	@Column(name = "SHIPPING_COST")
    private BigDecimal shippingCost;
	
	@Column(name = "TOTAL_AMOUNT")
    private BigDecimal totalAmount;

	@Column(name = "ORDER_STATUS")
	@Convert(converter = OrderStatusConverter.class)
	private OrderStatus orderStatus;
	
	@ManyToOne
	@JoinColumn(name = "PAYMENT_METHOD_KEY", referencedColumnName = "PAYMENT_METHOD_KEY")
	private PaymentMethod paymentMethod;
	
	@ManyToOne
    @JoinColumn(name = "SHIPPING_METHOD_KEY", referencedColumnName = "SHIPPING_METHOD_KEY")
	private ShippingMethod shippingMethod;
	
	@OneToMany(cascade = CascadeType.PERSIST, mappedBy = "order")
	private List<OrderItem> items;
	
	public static enum OrderStatus implements BaseEnum<Character> {

		NEW('N'), CONFIRMED('C'), DECLINED('D');
	    
	    public final Character value;
	    
	    private OrderStatus(Character value) {
	        this.value = value;
	    }
	    
	    public static OrderStatus valueOf(Character value) {
	        if (value == null) {
	            throw BaseEnum.npeException(OrderStatus.class);
	        }
	        for (OrderStatus status : OrderStatus.values()) {
	            if (value.equals(status.value)) {
	                return status;
	            }
	        }
	        throw BaseEnum.unknownValueException(OrderStatus.class, value);
	    }
	    
	    @Override
	    public Character getValue() {
	        return value;
	    }
	}
	
}
