import {NavigationService} from '../../service/navigation.service';
import {Component, OnInit} from '@angular/core';
import {MenuItem} from './menu-item';

@Component({
  selector: 'app-side-menu',
  templateUrl: './side-menu.component.html',
  styleUrls: ['./side-menu.component.css']
})
export class SideMenuComponent implements OnInit {

  mainMenu: MenuItem[];

  constructor(private navigationService: NavigationService) {}

  ngOnInit() {
    this.navigationService.loadMainMenu().subscribe(mainMenu => {
      this.mainMenu = mainMenu;
    });

  }

}
