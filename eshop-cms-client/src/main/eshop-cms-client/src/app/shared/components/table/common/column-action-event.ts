export interface ColumnActionEvent {
    columnId: string;
    filterOptions: {
      name: string,
      values: string[]
    }[];
    sortBy: string;
    sortDirection: string;
}
