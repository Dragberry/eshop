import { Component, Input } from '@angular/core';
import { TableFilter } from '../table-filter';

@Component({
  selector: 'app-table-list-filter',
  templateUrl: './table-list-filter.component.html',
  styleUrls: ['./table-list-filter.component.css']
})
export class TableListFilterComponent extends TableFilter {

  @Input()
  columnId: string;

  selectedOptions: {selected: boolean, option: {value: any, name: string}}[];

  @Input()
  set options(opts: {value: any, name: string}[]) {
    this.selectedOptions = opts.map(opt => {
      return {selected: false, option: opt};
    });
  }

  getSelectedValues(): {name: string, values: string[]}[] {
    return [{name: this.columnId, values: this.selectedOptions.filter(opt => opt.selected).map(opt => opt.option.value)}];
  }

  selectAll(): void {
    this.selectedOptions.forEach(opt => opt.selected = true);
  }

  resetAll(): void {
    this.selectedOptions.forEach(opt => opt.selected = false);
  }
}
