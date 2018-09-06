package org.dragberry.eshop.dal.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.dragberry.eshop.dal.entity.ShippingMethod.Status;

@Converter
public class ShippingMethodStatusConverter implements AttributeConverter<Status, Character> {

    @Override
    public Character convertToDatabaseColumn(Status attribute) {
        return attribute.value;
    }

    @Override
    public Status convertToEntityAttribute(Character dbData) {
        return Status.valueOf(dbData);
    }
}
