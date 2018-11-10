import { Page } from './page';
import { SortDirection } from '../components/table/common/sort-direction';

export const DEFAULT_PAGE_NUMBER = 1;
export const DEFAULT_PAGE_SIZE = 20;

export class DataTableState<T> {
  initialized = false;
  page?: Page<T>;
  pageSize?: number = DEFAULT_PAGE_SIZE;
  pageNumber?: number = DEFAULT_PAGE_NUMBER;
  sortBy?: string;
  sortDirection?: string = SortDirection.UNSORTED;
  filters?: Map<string, {name: string, values: string[]}[]> = new Map();
}
