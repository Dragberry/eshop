import { DataTableState } from 'src/app/shared/model/data-table-state';
import { ProductArticle } from 'src/app/catalog/model/product-article';
import { ProductCategory } from 'src/app/catalog/model/product-category';

export class ProductListState {
  dataTableState?: DataTableState<ProductArticle>;
  categoryTree?: ProductCategory[];
  selectedCategory?: ProductCategory;
}
