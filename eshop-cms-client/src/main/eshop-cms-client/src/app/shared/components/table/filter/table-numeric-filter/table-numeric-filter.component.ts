import { Component, Input } from '@angular/core';
import { TableRangeFilter } from '../table-range-filter';

@Component({
  selector: 'app-table-numeric-filter',
  templateUrl: './table-numeric-filter.component.html',
  styleUrls: ['./table-numeric-filter.component.css']
})
export class TableNumericFilterComponent extends TableRangeFilter<number> {

  toString(value: number): string {
    return value ? value.toString() : null;
  }
}
