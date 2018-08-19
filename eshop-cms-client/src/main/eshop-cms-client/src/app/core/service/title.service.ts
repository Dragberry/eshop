import {Injectable} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {Observable, Subscriber} from 'rxjs';

@Injectable()
export class TitleService {

  source: Observable<string>;
  subscriber: Subscriber<string>;

  constructor(private translate: TranslateService) {
    this.source = new Observable((subscriber: Subscriber<string>) => {
      this.subscriber = subscriber;
    });
  }

  setTitle(title: string): void {
    this.subscriber.next(title);
  }

  setTitleKey(titleKey: string): void {
    this.translate.get(titleKey).subscribe(str => this.subscriber.next(str));
  }
}
