package org.dragberry.eshop.dal.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.dragberry.eshop.dal.entity.ProductArticle.SaleStatus;

@Converter
public class SaleStatusConverter implements AttributeConverter<SaleStatus, Character> {

    @Override
    public Character convertToDatabaseColumn(SaleStatus attribute) {
        return attribute.value;
    }

    @Override
    public SaleStatus convertToEntityAttribute(Character dbData) {
        return SaleStatus.valueOf(dbData);
    }
}
