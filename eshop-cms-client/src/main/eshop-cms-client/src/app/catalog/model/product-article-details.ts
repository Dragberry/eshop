import { File } from 'src/app/shared/model/file';
import { ProductCategory } from './product-category';
import { ProductSaleStatus } from './product-sale-status';
import { BooleanAttribute, ListAttribute, NumericAttribute, StringAttribute } from './attributes';

export class ProductArticleDetails {
  id: number;
  title: string;
  article: string;
  reference: string;
  description: string;
  descriptionFull: string;
  tagTitle: string;
  tagKeywords: string;
  tagDescription: string;
  status: ProductSaleStatus;
  mainImageId: number;
  images: File[];
  mainCategoryId: number;
  categoryTree: ProductCategory[];

  booleanAttributes: BooleanAttribute[];
  listAttributes: ListAttribute[];
  numericAttributes: NumericAttribute[];
  stringAttributes: StringAttribute[];
}
