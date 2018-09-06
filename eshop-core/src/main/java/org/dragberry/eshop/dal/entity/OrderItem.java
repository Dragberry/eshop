package org.dragberry.eshop.dal.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "ORDER_ITEM")
@TableGenerator(
		name = "ORDER_ITEM_GEN", 
		table = "GENERATOR",
		pkColumnName = "GEN_NAME", 
		pkColumnValue = "ORDER_ITEM_GEN",
		valueColumnName = "GEN_VALUE",
		initialValue = 1000,
		allocationSize = 1)
@Setter
@Getter
public class OrderItem extends BaseEntity {

	private static final long serialVersionUID = 6817451222642163283L;

	@Id
	@Column(name = "ORDER_ITEM_KEY")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ORDER_ITEM_GEN")
	private Long entityKey;
	
	@ManyToOne
	@JoinColumn(name = "ORDER_KEY", referencedColumnName = "ORDER_KEY")
	private Order order;
	
	@ManyToOne
	@JoinColumn(name = "PRODUCT_KEY", referencedColumnName = "PRODUCT_KEY")
	private Product product;

	@Column(name = "PRICE")
	private BigDecimal price;
	
	@Column(name = "QUANTITY")
	private Integer quantity;
	
	@Column(name = "TOTAL_AMOUNT")
	private BigDecimal totalAmount;
}
