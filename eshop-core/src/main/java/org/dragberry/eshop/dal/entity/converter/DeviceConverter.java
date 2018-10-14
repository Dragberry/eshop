package org.dragberry.eshop.dal.entity.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.dragberry.eshop.dal.entity.RequestLog.Device;

@Converter
public class DeviceConverter implements AttributeConverter<Device, Character> {

    @Override
    public Character convertToDatabaseColumn(Device attribute) {
        return attribute.value;
    }

    @Override
    public Device convertToEntityAttribute(Character dbData) {
        return Device.valueOf(dbData);
    }
}
