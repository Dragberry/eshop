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
  activeMenu: MenuItem;
  activeSubMenu: MenuItem;

  constructor(private navigationService: NavigationService) {}

  ngOnInit() {
    this.navigationService.loadMainMenu().then(mainMenu => {
      this.mainMenu = mainMenu;
    });
  }

  navigate(menuItem: MenuItem, subMenu = false): void {
    if (subMenu) {
      this.activeSubMenu = this.activeSubMenu === menuItem ? null : menuItem;
    } else {
      this.activeSubMenu = null;
      this.activeMenu = this.activeMenu === menuItem ? null : menuItem;
    }
    if (!menuItem.subMenu || menuItem.subMenu.length === 0) {
      this.navigationService.navigate(menuItem.action);
    }
  }

}
