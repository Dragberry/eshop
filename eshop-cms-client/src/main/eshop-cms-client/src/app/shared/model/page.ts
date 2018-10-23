export interface Page<T> {
  pageNumber: number;
  pageSize: number;
  pageCount: number;
  total: number;
  content: T[];
}
