import {  } from './../../model/data-table-state';
import { ColumnActionEvent } from './common/column-action-event';
import { HttpParams } from '@angular/common/http';
import { SortDirection } from './common/sort-direction';
import { PageableEvent } from './common/pageable-event';
import { ViewChildren, QueryList, AfterViewInit } from '@angular/core';
import { TableActionColumnComponent } from './table-action-column/table-action-column.component';
import { Page, DataTableState, DEFAULT_PAGE_NUMBER, DEFAULT_PAGE_SIZE } from 'src/app/shared/model';

export abstract class DataTableHolder<T> implements AfterViewInit {

  @ViewChildren(TableActionColumnComponent)
  columns!: QueryList<TableActionColumnComponent>;

  private pageSize: number;
  private pageNumber: number;

  private sortBy: string;
  private sortDirection: string;

  private filters: Map<string, {name: string, values: string[]}[]> = new Map();

  page: Page<T>;

  ngAfterViewInit(): void {
    this.columns.changes.subscribe(columns => {
      columns.forEach(column => {
        Promise.resolve().then(() => {
          const sortDirection: SortDirection = this.sortBy != null && this.sortBy === column.sortBy
              ? SortDirection[this.sortDirection.toUpperCase()]
              : SortDirection.UNSORTED;
            column.restore(sortDirection, this.filters.get(column.columnId));
        });
      });
    });
  }

  getDataTableState(): DataTableState<T> {
    return {
      initialized: true,
      page: this.page,
      pageSize: this.pageSize,
      pageNumber: this.pageNumber,
      sortBy: this.sortBy,
      sortDirection: this.sortDirection,
      filters: this.filters
    };
  }

  setDataTableState(state: DataTableState<T>): void {
    this.pageSize = state.pageSize;
    this.pageNumber = state.pageNumber;
    this.sortBy = state.sortBy;
    this.sortDirection = state.sortDirection;
    this.filters = state.filters;
    this.fetchPage();
  }

  public fetchPage(): void {
    let params = new HttpParams()
      .set('pageNumber', this.pageNumber.toString())
      .set('pageSize', this.pageSize.toString());
    if (this.sortBy && SortDirection.UNSORTED !== this.sortDirection) {
      params = params.append('sort', `${this.sortBy}[${this.sortDirection}]`);
    }
    this.filters.forEach((fieldFilters) => {
      if (fieldFilters) {
        fieldFilters.filter(filedFilter => filedFilter != null).forEach(filedFilter => {
          if (filedFilter.values) {
            filedFilter.values.filter(value => value != null).forEach(value => {
              params = params.append(filedFilter.name, value);
            });
          }
        });
      }
    });
    this.invokeService(this.enrichParams(params))
    .then(page => this.page = page)
    .catch(() => console.log('An error has occured!'));
  }

  protected abstract invokeService(params: HttpParams): Promise<Page<T>>;

  protected enrichParams(params: HttpParams): HttpParams {
    return params;
   }

  onPageableEvent(event: PageableEvent): void {
    this.pageSize = event.pageSize;
    this.pageNumber = event.pageNumber;
    this.fetchPage();
  }

  onColumnActionEvent(event: ColumnActionEvent): void {
    this.pageNumber = DEFAULT_PAGE_NUMBER;
    this.sortBy = event.sortBy;
    this.sortDirection = event.sortDirection;
    this.columns.forEach(column => {
      if (column.sortBy !== this.sortBy) {
        column.clearSorting();
      }
    });
    this.filters.set(event.columnId, event.filterOptions);
    this.fetchPage();
  }

  resetAll(): void {
    this.columns.forEach(column => {
      column.clearSorting();
      column.clearFilter();
      this.sortBy = null;
      this.sortDirection = SortDirection.UNSORTED;
      this.filters.clear();
    });
    this.pageNumber = DEFAULT_PAGE_NUMBER;
    this.pageSize = DEFAULT_PAGE_SIZE;
    this.fetchPage();
  }
}
