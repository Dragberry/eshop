import { DataTableState } from 'src/app/shared/model/data-table-state';
import { ProductArticle, ProductCategory } from 'src/app/catalog/model';

export class ProductListState {
  dataTableState?: DataTableState<ProductArticle>;
  categoryTree?: ProductCategory[];
  selectedCategory?: ProductCategory;
  searchQuery?: string;
}
