import { Component, Input } from '@angular/core';
import { TableFilter } from '../table-filter';

@Component({
  selector: 'app-table-list-filter',
  templateUrl: './table-list-filter.component.html',
  styleUrls: ['./table-list-filter.component.css']
})
export class TableListFilterComponent extends TableFilter {

  selectedOptions: {selected: boolean, option: {value: any, name: string}}[];
  private sourceSelectedOptions: {selected: boolean, option: {value: any, name: string}}[];

  @Input()
  set options(opts: {value: any, name: string}[]) {
    this.sourceSelectedOptions = opts.map(opt => {
      return {selected: false, option: opt};
    });
    this.selectedOptions = opts.map(opt => {
      return {selected: false, option: opt};
    });
  }

  getSelectedValues(): {name: string, values: string[]}[] {
    this.sourceSelectedOptions = this.selectedOptions.map(opt => {
      return {selected: opt.selected, option: {value: opt.option.value, name: opt.option.name}};
    });
    return [{name: `attribute[${this.columnId}]`, values: this.selectedOptions.filter(opt => opt.selected).map(opt => opt.option.value)}];
  }

  reset(): void {
    this.selectedOptions = this.sourceSelectedOptions.map(opt => {
      return {selected: opt.selected, option: {value: opt.option.value, name: opt.option.name}};
    });
  }

  selectAll(): void {
    this.selectedOptions.forEach(opt => opt.selected = true);
  }

  clearAll(): void {
    this.selectedOptions.forEach(opt => opt.selected = false);
  }
}
