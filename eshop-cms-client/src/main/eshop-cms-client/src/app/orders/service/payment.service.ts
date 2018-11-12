import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { PaymentMethod, PaymentMethodStatus } from '../model/payment-method';
import { HttpDelegateService } from 'src/app/core/http/http-delegate.service';

@Injectable()
export class PaymentService {

    private readonly PAYMENT_URL = 'payment';
    private readonly PAYMENT_METHODS_URL = `${this.PAYMENT_URL}/method/list`;

    constructor(private httpService: HttpDelegateService) {}

    getActivePaymentMethods(): Promise<PaymentMethod[]> {
        return this.httpService.get<PaymentMethod[]>(this.PAYMENT_METHODS_URL, {
            params: new HttpParams().set('status', PaymentMethodStatus.ACTIVE)
        });
    }

    getAllPaymentMethods(): Promise<PaymentMethod[]> {
        return this.httpService.get<PaymentMethod[]>(this.PAYMENT_METHODS_URL, {
            params: new HttpParams()
                .append('status', PaymentMethodStatus.ACTIVE)
                .append('status', PaymentMethodStatus.INACTIVE)
        });
    }
}
