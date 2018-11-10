import { NavigationService } from './../../service/navigation.service';
import { Component, OnInit } from '@angular/core';
import { Router, RouterEvent, NavigationEnd, ActivationEnd } from '@angular/router';
import { OrderListComponent } from 'src/app/orders/components/order-list/order-list.component';

@Component({
  selector: 'app-breadcrumb',
  templateUrl: './breadcrumb.component.html',
  styleUrls: ['./breadcrumb.component.css']
})
export class BreadcrumbComponent implements OnInit {

  links: { title: string, link: string }[];

  constructor(
    private navigationService: NavigationService,
    private router: Router) { }

  ngOnInit() {
    this.links = [];
    this.router.events.subscribe((event: RouterEvent) => {
      if (event instanceof ActivationEnd) {
        const activationEnd = <ActivationEnd> event;
        if (activationEnd.snapshot.children.length === 0) {
          if (activationEnd.snapshot.component instanceof OrderListComponent) {
            const screen = <OrderListComponent> activationEnd.snapshot.component;
          }
        }
      }
      if (event instanceof  NavigationEnd) {
        this.links.push({title: event.urlAfterRedirects, link: event.urlAfterRedirects });
        console.log(event);
      }
    });
  }

}
