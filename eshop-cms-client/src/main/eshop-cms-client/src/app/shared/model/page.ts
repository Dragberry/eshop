export interface Page<T> {
  pageNumber: number;
  pageSize: number;
  totalPages: number;
  total: number;
  content: T[];
}
