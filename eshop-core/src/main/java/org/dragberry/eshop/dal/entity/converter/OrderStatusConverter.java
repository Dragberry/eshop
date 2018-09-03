package org.dragberry.eshop.dal.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.dragberry.eshop.dal.entity.Order.OrderStatus;

@Converter
public class OrderStatusConverter implements AttributeConverter<OrderStatus, Character> {

    @Override
    public Character convertToDatabaseColumn(OrderStatus attribute) {
        return attribute.value;
    }

    @Override
    public OrderStatus convertToEntityAttribute(Character dbData) {
        return OrderStatus.valueOf(dbData);
    }
}
