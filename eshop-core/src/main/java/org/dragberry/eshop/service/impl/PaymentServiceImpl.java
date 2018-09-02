package org.dragberry.eshop.service.impl;

import java.util.List;

import org.dragberry.eshop.model.payment.PaymentMethod;
import org.dragberry.eshop.service.PaymentService;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

	@Override
	public List<PaymentMethod> getPaymentMethods() {
		return List.of(
				new PaymentMethod(1L, "Наличными при получении заказа", "Оплата наличными деньгами (белорусские рубли) при получении товара"),
				new PaymentMethod(2L, "Банковской картой при получении заказа", "Оплата картой любого банка: Visa, Visa Electron, MasterCard,  Maestro, Белкарт, карта рассрочки Халва (3 месяца) и Магнит (2 месяца)"),
				new PaymentMethod(3L, "Оплата онлайн через сайт: bePaid.by", "Оплата картой любого банка при оформлении заказа через корзину. После нажатия кнопки «ПОДТВЕРДИТЬ ЗАКАЗ» вы перейдете на специальную защищенную платежную страницу процессинговой системы bePaid."),
				new PaymentMethod(4L, "Рассрочка без переплат на 12 месяцев", "Рассрочку от Беларусбанка на 12 месяцев. Мы подготавливаем для Вас счет-фактуру, с которой необходимо обратиться в любое отделение Беларусбанка для оформления. Нужен только паспорт! "));
	}

}
