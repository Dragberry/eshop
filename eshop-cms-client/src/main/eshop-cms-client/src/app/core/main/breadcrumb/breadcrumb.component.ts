import { NavigationService } from './../../service/navigation.service';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, ActivatedRouteSnapshot, Router, RouterEvent, NavigationEnd } from '@angular/router';

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
      if (event instanceof  NavigationEnd) {
        this.links.push({title: event.urlAfterRedirects, link: event.urlAfterRedirects });
        console.log(event);
      }
    });
  }

}
