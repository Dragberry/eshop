import {TranslateService} from '@ngx-translate/core';
import {Component, OnInit} from '@angular/core';
import {AuthenticationService, LOGIN_URL} from '../auth/authentication.service';
import {TitleService} from '../service/title.service';
import { Title } from '@angular/platform-browser';
import {Router} from '@angular/router';

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
    private router: Router,
    private translate: TranslateService,
    private title: Title,
    private titleService: TitleService) {}

  ngOnInit() {
    this.onLanguageChanged(localStorage.getItem(CURRENT_LANG) || EN);
    this.loggedUser = this.authService.getUserDetails();
    this.titleService.source.subscribe(title => {
      this.titleString = title;
      this.title.setTitle(title);
    });
    console.log(this.loggedUser);
  }

  logout(): void {
    this.authService.logout();
    this.router.navigate([LOGIN_URL]);
  }

  onLanguageChanged(lang: string): void {
    localStorage.setItem(CURRENT_LANG, lang);
    this.currentLang = lang;
    this.translate.setDefaultLang(lang);
    this.translate.use(lang);
  }
}
