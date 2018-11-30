import { Component } from '@angular/core';
import { TableRangeFilter } from '../table-range-filter';

@Component({
  selector: 'app-table-date-filter',
  templateUrl: './table-date-filter.component.html'
})
export class TableDateFilterComponent extends TableRangeFilter<Date> {

  toString(value: Date): string {
    return value ? value.toISOString().slice(0, -5) : null;
  }
}
