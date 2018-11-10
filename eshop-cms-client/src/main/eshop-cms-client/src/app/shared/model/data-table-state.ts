import { Page } from './page';

export class DataTableState<T> {
  page: Page<T>;
  pageSize?: number;
  pageNumber?: number;
  sortBy?: string;
  sortDirection?: string;
  filters?: Map<string, {name: string, values: string[]}[]>;
}
