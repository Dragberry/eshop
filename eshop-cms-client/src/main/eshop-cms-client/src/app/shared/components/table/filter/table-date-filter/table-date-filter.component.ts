import { Component } from '@angular/core';
import { TableRangeFilter } from '../table-range-filter';

@Component({
  selector: 'app-table-date-filter',
  templateUrl: './table-date-filter.component.html',
  styleUrls: ['./table-date-filter.component.css']
})
export class TableDateFilterComponent extends TableRangeFilter<Date> {

  toString(value: Date): string {
    return value ? value.toISOString() : null;
  }
}
