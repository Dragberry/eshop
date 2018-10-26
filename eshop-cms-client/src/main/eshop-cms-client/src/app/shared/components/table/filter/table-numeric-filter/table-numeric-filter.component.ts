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

  from: string;
  to: string;

  getSelectedValues(): {name: string, values: string[]}[] {
    return [
      {name: `${this.columnId}[from]`, values: [this.from]},
      {name: `${this.columnId}[to]`, values: [this.to]}
    ];
  }

  resetAll(): void {
    this.from = null;
    this.to = null;
  }
}
