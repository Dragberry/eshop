import { DataTableState } from 'src/app/shared/model/data-table-state';
import { ProductArticle } from 'src/app/catalog/model/product-article';

export class ProductListState {
  dataTableState?: DataTableState<ProductArticle>;
}
