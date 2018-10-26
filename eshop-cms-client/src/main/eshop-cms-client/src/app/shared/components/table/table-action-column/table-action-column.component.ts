import { Component, Input, Output, EventEmitter, ViewChild } from '@angular/core';
import { TableListFilterComponent } from '../table-list-filter/table-list-filter.component';

@Component({
  selector: 'app-table-action-column',
  templateUrl: './table-action-column.component.html',
  styleUrls: ['./table-action-column.component.css']
})
export class TableActionColumnComponent {

  @ViewChild(TableListFilterComponent)
  filterComponent: TableListFilterComponent;

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
  selectedOptions: any[];

  @Output()
  columnAction: EventEmitter<any> = new EventEmitter();

  constructor() { }

  applyFilter(): void {
    this.selectedOptions = this.filterComponent.selectedOptions;
    this.emitActionEvent();
  }

  hasFilter(): boolean {
    return this.selectedOptions && this.selectedOptions.find(opt => opt.selected) != null;
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
      filterOptions: this.selectedOptions,
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
