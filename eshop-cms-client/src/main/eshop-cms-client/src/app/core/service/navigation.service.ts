import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { MenuItem } from '../main/side-menu/menu-item';
import { Injectable } from '@angular/core';
import { Observable, Subscriber } from 'rxjs';

@Injectable()
export class NavigationService {

  titleSource: Observable<string>;
  titleSubscribe: Subscriber<string>;

  linksSource: Observable<{title: string, link: string}>;
  linksSubscribe: Subscriber<{title: string, link: string}>;

  constructor(
    private router: Router,
    private translateService: TranslateService) {
    this.titleSource = new Observable((subscribe: Subscriber<string>) => {
      this.titleSubscribe = subscribe;
    });
    this.linksSource = new Observable((subscribe: Subscriber<{title: string, link: string}>) => {
      this.linksSubscribe = subscribe;
    });
  }

  loadMainMenu(): Promise<MenuItem[]> {
    return new Promise(resolve => {
      resolve([
        {id: 'dashboard', title: 'main-menu.dashboard', action: '/dashboard', roles: [], subMenu: []},
        {id: 'orders', title: 'main-menu.orders', action: '/orders', roles: [], subMenu: []},
        {id: 'catalog', title: 'main-menu.catalog', roles: [], subMenu: [
          {id: 'categories', title: 'main-menu.categories', action: '/catalog/categories', roles: [], subMenu: []},
          {id: 'products', title: 'main-menu.products', action: '/catalog/products', roles: [], subMenu: []}
        ]},
        {id: 'customers', title: 'main-menu.customers', action: '/customers', roles: [], subMenu: []}
      ]);
    });
  }

  navigate(url: string): Promise<boolean> {
    this.linksSubscribe.next();
    return this.router.navigate([url]);
  }

  navigateBack(url: string): Promise<boolean> {
    this.linksSubscribe.next();
    return this.router.navigate([url]);
  }

  currentScreen(url: string, titleKey: string, titleParams?: Object): void {
    this.translateService.get(titleKey, titleParams).subscribe(title => {
      this.linksSubscribe.next({title: title, link: url});
      this.titleSubscribe.next(title);
    });
  }
}

