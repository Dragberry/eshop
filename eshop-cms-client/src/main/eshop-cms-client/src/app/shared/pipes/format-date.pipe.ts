import { Pipe, PipeTransform } from '@angular/core';
import { DatePipe } from '@angular/common';

const DATE_TIME_FORMAT = 'yyyy/MM/dd hh:mm:ss';

@Pipe({
  name: 'formatDate'
})
export class FormatDate implements PipeTransform {

  constructor(private datePipe: DatePipe) {}

  transform(value: string): string {
    return this.datePipe.transform(new Date(value), DATE_TIME_FORMAT);
  }

}
