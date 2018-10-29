import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PaymentMethod, PaymentMethodStatus } from '../model/payment-method';

@Injectable()
export class PaymentService {

    private readonly PAYMENT_URL = 'payment';
    private readonly PAYMENT_METHODS_URL = `${this.PAYMENT_URL}/method/list`;

    constructor(private http: HttpClient) {}

    getActivePaymentMethods(): Observable<PaymentMethod[]> {
        return this.http.get<PaymentMethod[]>(this.PAYMENT_METHODS_URL, {
            params: new HttpParams().set('status', PaymentMethodStatus.ACTIVE)
        });
    }

    getAllPaymentMethods(): Observable<PaymentMethod[]> {
        return this.http.get<PaymentMethod[]>(this.PAYMENT_METHODS_URL, {
            params: new HttpParams()
                .append('status', PaymentMethodStatus.ACTIVE)
                .append('status', PaymentMethodStatus.INACTIVE)
        });
    }
}
