package org.dragberry.eshop.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.dragberry.eshop.model.delivery.DeliveryMethod;
import org.dragberry.eshop.service.DeliveryService;
import org.springframework.stereotype.Service;

@Service
public class DeliveryServiceImpl implements DeliveryService {

	@Override
	public List<DeliveryMethod> getDeliveryMethods() {
		return List.of(
				new DeliveryMethod(1L, "Курьером по Минску", "Доставка курьером по Минску в день заказа", BigDecimal.ZERO),
				new DeliveryMethod(2L, "Курьером по Беларуси", "Доставка курьером по всем городам и населенным пунктам Беларуси. Срок доставки: 1-2 дня.", new BigDecimal(8)),
				new DeliveryMethod(3L, "Доставка почтой", "Доставка в ближайшее почтовое отделение. Срок доставки: 2-3 рабочих дня.", new BigDecimal(5)));
	}

}
