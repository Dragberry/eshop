import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { TableFilter } from '../filter/table-filter';

@Component({
  selector: 'app-table-action-column',
  templateUrl: './table-action-column.component.html',
  styleUrls: ['./table-action-column.component.css']
})
export class TableActionColumnComponent {

  @ViewChild('tableFilter')
  filterComponent: TableFilter;

  @Input()
  title: string;

  @Input()
  columnId: string;

  @Input()
  sortBy: string;
  sortDirection = SortDirection.UNSORTED;
  sortClass = 'fa-sort';

  @Input()
  filter: string;
  @Input()
  filterOptions: {value: any, name: string}[];
  selectedFilterValues: {name: string, values: string[]}[];

  @Output()
  columnAction: EventEmitter<any> = new EventEmitter();

  constructor() { }

  applyFilter(): void {
    this.selectedFilterValues = this.filterComponent.getSelectedValues();
    this.emitActionEvent();
  }

  resetFilter(): void {
    this.filterComponent.reset();
  }

  hasFilter(): boolean {
    return this.selectedFilterValues && this.selectedFilterValues
      .find(filterValue => !filterValue.values || filterValue.values.length !== 0) != null;
  }

  toggleSorting(): void {
    switch (this.sortDirection) {
      case SortDirection.UNSORTED:
        this.sortDirection = SortDirection.ASC;
        this.sortClass = 'fa-sort-down';
        break;
      case SortDirection.ASC:
        this.sortDirection = SortDirection.DESC;
        this.sortClass = 'fa-sort-up';
        break;
      case SortDirection.DESC:
        this.sortDirection = SortDirection.UNSORTED;
        this.sortClass = 'fa-sort';
        break;
      default:
        this.sortDirection = SortDirection.UNSORTED;
        this.sortClass = 'fa-sort';
        break;
    }
    this.emitActionEvent();
  }

  private emitActionEvent() {
    this.columnAction.emit({
      columnId: this.columnId,
      filterOptions: this.selectedFilterValues,
      sortBy: this.sortBy,
      sortDirection: this.sortDirection
    });
  }
}

enum SortDirection {
  UNSORTED = 'unsorted',
  ASC = 'asc',
  DESC = 'desc'
}
