import { DateService } from './../../core/service/date.service';
import { Pipe, PipeTransform } from '@angular/core';
import { DatePipe } from '@angular/common';

@Pipe({
  name: 'formatDate'
})
export class FormatDate implements PipeTransform {

  constructor(private dateService: DateService) {}

  transform(value: string): string {
    return this.dateService.formatDateString(value);
  }

}
