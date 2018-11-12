import { ColumnActionEvent } from './../common/column-action-event';
import { SortDirection } from './../common/sort-direction';
import { Component, EventEmitter, Input, Output, ViewChild } from '@angular/core';
import { TableFilter } from '../filter/table-filter';

const SORT_CLASSES = new Map<SortDirection, string>();
SORT_CLASSES.set(SortDirection.UNSORTED, 'fa-sort');
SORT_CLASSES.set(SortDirection.ASC, 'fa-sort-down');
SORT_CLASSES.set(SortDirection.DESC, 'fa-sort-up');

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

  @Input()
  filter: string;
  @Input()
  filterOptions: {value: any, name: string}[];
  selectedFilterValues: {name: string, values: string[]}[];

  @Output()
  columnAction: EventEmitter<ColumnActionEvent> = new EventEmitter();

  constructor() { }

  restore(sortDirection: SortDirection, filterValues: {name: string; values: string[]}[]): void {
    this.sortDirection = sortDirection;
    this.selectedFilterValues = filterValues;
  }

  applyFilter(): void {
    this.selectedFilterValues = this.filterComponent.getSelectedValues();
    this.emitActionEvent();
  }

  resetFilter(): void {
    if (this.filterComponent) {
      this.filterComponent.reset();
    }
  }

  clearFilter(): void {
    if (this.filterComponent) {
      this.filterComponent.clearAll();
      this.selectedFilterValues = [];
    }
  }

  hasFilter(): boolean {
    return this.selectedFilterValues && this.selectedFilterValues
      .find(filterValue => !filterValue.values || filterValue.values.length !== 0) != null;
  }

  clearSorting(): void {
    this.sortDirection = SortDirection.UNSORTED;
  }

  getSortClass(): string {
    return SORT_CLASSES.get(this.sortDirection || SortDirection.UNSORTED);
  }

  toggleSorting(): void {
    switch (this.sortDirection) {
      case SortDirection.UNSORTED:
        this.sortDirection = SortDirection.ASC;
        break;
      case SortDirection.ASC:
        this.sortDirection = SortDirection.DESC;
        break;
      case SortDirection.DESC:
        this.sortDirection = SortDirection.UNSORTED;
        break;
      default:
        this.sortDirection = SortDirection.UNSORTED;
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
