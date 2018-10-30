import { Pipe, PipeTransform, Inject, LOCALE_ID } from '@angular/core';
import { DatePipe } from '@angular/common';

const DATE_TIME_FORMAT = 'yyyy/MM/dd hh:mm:ss';

@Pipe({
  name: 'formatDate'
})
export class FormatDate implements PipeTransform {

  constructor(@Inject(LOCALE_ID) private locale: string) {}

  transform(value: string): string {
    return new DatePipe(this.locale).transform(new Date(value), DATE_TIME_FORMAT);
  }

}
