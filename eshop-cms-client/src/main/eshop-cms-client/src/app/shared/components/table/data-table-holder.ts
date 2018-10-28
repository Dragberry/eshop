import { ColumnActionEvent } from './common/column-action-event';
import { HttpParams } from '@angular/common/http';
import { SortDirection } from './common/sort-direction';
import { Page } from '../../model/page';
import { PageableEvent } from './common/pageable-event';
import { Observable } from 'rxjs';

const DEFAULT_PAGE_NUMBER = 1;
const DEFAULT_PAGE_SIZE = 20;

export abstract class DataTableHolder<T> {

  private pageSize = DEFAULT_PAGE_SIZE;
  private pageNumber = DEFAULT_PAGE_NUMBER;

  private sortBy: string;
  private sortDirection: string;

  private filters: Map<string, {name: string, values: string[]}[]> = new Map();

  protected page: Page<T>;

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
    this.invokeService(params).subscribe(page => this.page = page);
  }

  protected abstract invokeService(params: HttpParams): Observable<Page<T>>;

  onPageableEvent(event: PageableEvent): void {
    this.pageSize = event.pageSize;
    this.pageNumber = event.pageNumber;
    this.fetchPage();
  }


  onColumnActionEvent(event: ColumnActionEvent): void {
    this.pageNumber = DEFAULT_PAGE_NUMBER;
    this.sortBy = event.sortBy;
    this.sortDirection = event.sortDirection;
    this.filters.set(event.columnId, event.filterOptions);
    this.fetchPage();
  }
}
