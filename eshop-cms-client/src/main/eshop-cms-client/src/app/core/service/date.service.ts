import { DatePipe } from '@angular/common';
import { Inject, LOCALE_ID, Injectable } from '@angular/core';

export const DATE_TIME_FORMAT = 'yyyy/MM/dd hh:mm:ss';

@Injectable()
export class DateService {

  constructor(@Inject(LOCALE_ID) private locale: string) {}

  formatDate(value: Date): string {
    try {
      return new DatePipe(this.locale).transform(value, DATE_TIME_FORMAT);
    } catch (e) {
      return '--';
    }
  }

  formatDateString(value: string): string {
    try {
      return new DatePipe(this.locale).transform(new Date(value), DATE_TIME_FORMAT);
    } catch (e) {
      return '--';
    }
  }

}
