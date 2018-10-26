import { Component, Input } from '@angular/core';
import { TableFilter } from '../table-filter';

@Component({
  selector: 'app-table-numeric-filter',
  templateUrl: './table-numeric-filter.component.html',
  styleUrls: ['./table-numeric-filter.component.css']
})
export class TableNumericFilterComponent extends TableFilter {

  @Input()
  columnId: string;

  sourceFrom: string;
  sourceTo: string;

  from: string;
  to: string;

  getSelectedValues(): {name: string, values: string[]}[] {
    this.sourceFrom = this.from;
    this.sourceTo = this.to;
    return [
      {name: `${this.columnId}[from]`, values: [this.from]},
      {name: `${this.columnId}[to]`, values: [this.to]}
    ];
  }

  reset(): void {
    this.from = this.sourceFrom;
    this.to = this.sourceTo;
  }

  clearAll(): void {
    this.from = null;
    this.to = null;
  }
}
