import {TranslateService} from '@ngx-translate/core';
import {Component, OnInit} from '@angular/core';
import {AuthenticationService} from '../auth/authentication.service';
import { Title } from '@angular/platform-browser';
import { NavigationService } from '../service/navigation.service';

const CURRENT_LANG = 'currentLang';
const EN = 'en';
const RU = 'ru';

@Component({
  selector: 'app-main',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.css']
})
export class MainComponent implements OnInit {

  isCollapsed = true;

  languages = [EN, RU];
  currentLang = EN;

  loggedUser: any;
  titleString: string;

  constructor(
    private authService: AuthenticationService,
    private navigationService: NavigationService,
    private translate: TranslateService,
    private title: Title) {}

  ngOnInit() {
    this.onLanguageChanged(localStorage.getItem(CURRENT_LANG) || EN);
    this.loggedUser = this.authService.getUserDetails();
    this.navigationService.titleSource.subscribe(title => {
      this.titleString = title;
      this.title.setTitle(title);
    });
  }

  logout(): void {
    this.authService.logout();
  }

  onLanguageChanged(lang: string): void {
    localStorage.setItem(CURRENT_LANG, lang);
    this.currentLang = lang;
    this.translate.setDefaultLang(lang);
    this.translate.use(lang);
  }
}
