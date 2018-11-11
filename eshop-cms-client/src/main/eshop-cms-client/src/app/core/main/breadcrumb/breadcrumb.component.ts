import { NavigationService } from './../../service/navigation.service';
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

const DASHBOARD_LINK = '';

@Component({
  selector: 'app-breadcrumb',
  templateUrl: './breadcrumb.component.html',
  styleUrls: ['./breadcrumb.component.css']
})
export class BreadcrumbComponent implements OnInit {

  links: { id: number, title: string, link: string }[];

  constructor(
    private router: Router,
    private navigationService: NavigationService) { }

  ngOnInit() {
    this.links = [];
    this.navigationService.linksSource.subscribe(link => {
      if (link) {
        if (DASHBOARD_LINK === link.link) {
          this.links = [];
        }
        this.links.push({id: this.links.length, ...link});
      } else {
        this.links = this.links.slice(0, 1);
      }
    });
  }

  navigate(id: number): void {
    this.router.navigate([this.links[id].link]);
    console.log(this.links[id]);
    console.log(id);
    this.links = this.links.slice(0, id);
  }
}
