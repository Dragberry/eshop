import { Injectable } from '@angular/core';
import { HttpParams } from '@angular/common/http';
import { ShippingMethod, ShippingMethodStatus } from '../model/shipping-method';
import { HttpDelegateService } from 'src/app/core/http/http-delegate.service';

@Injectable()
export class ShippingService {

    private readonly SHIPPING_URL = 'shipping';
    private readonly SHIPPING_METHODS_URL = `${this.SHIPPING_URL}/method/list`;

    constructor(private httpService: HttpDelegateService) {}

    getActiveShippingMethods(): Promise<ShippingMethod[]> {
        return this.httpService.get<ShippingMethod[]>(this.SHIPPING_METHODS_URL, {
            params: new HttpParams().set('status', ShippingMethodStatus.ACTIVE)
        });
    }

    getAllShippingMethods(): Promise<ShippingMethod[]> {
        return this.httpService.get<ShippingMethod[]>(this.SHIPPING_METHODS_URL, {
            params: new HttpParams()
                .append('status', ShippingMethodStatus.ACTIVE)
                .append('status', ShippingMethodStatus.INACTIVE)
        });
    }
}
