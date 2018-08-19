import {MenuItem} from '../main/side-menu/menu-item';
import {Injectable} from '@angular/core';
import {from, Observable} from 'rxjs';

@Injectable()
export class NavigationService {

  loadMainMenu(): Observable<MenuItem[]> {
    return from([[
      {id: 'dashboard', title: 'main-menu.dashboard', action: '/dashboard', roles: [], subMenu: []},
      {id: 'products', title: 'main-menu.products', action: '/products', roles: [], subMenu: []},
      {id: 'orders', title: 'main-menu.orders', action: '/orders', roles: [], subMenu: []},
      {id: 'customers', title: 'main-menu.customers', action: '/customers', roles: [], subMenu: []}
    ]]);
  }
}

