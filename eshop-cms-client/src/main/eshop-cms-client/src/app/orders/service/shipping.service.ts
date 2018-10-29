import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ShippingMethod, ShippingMethodStatus } from '../model/shipping-method';

@Injectable()
export class ShippingService {

    private readonly SHIPPING_URL = 'shipping';
    private readonly SHIPPING_METHODS_URL = `${this.SHIPPING_URL}/method/list`;

    constructor(private http: HttpClient) {}

    getActiveShippingMethods(): Observable<ShippingMethod[]> {
        return this.http.get<ShippingMethod[]>(this.SHIPPING_METHODS_URL, {
            params: new HttpParams().set('status', ShippingMethodStatus.ACTIVE)
        });
    }

    getAllShippingMethods(): Observable<ShippingMethod[]> {
        return this.http.get<ShippingMethod[]>(this.SHIPPING_METHODS_URL, {
            params: new HttpParams()
                .append('status', ShippingMethodStatus.ACTIVE)
                .append('status', ShippingMethodStatus.INACTIVE)
        });
    }
}
