package org.dragberry.eshop.model.product;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumericAttributeTO extends AttributeTO<BigDecimal> {

	private String unit;
}
