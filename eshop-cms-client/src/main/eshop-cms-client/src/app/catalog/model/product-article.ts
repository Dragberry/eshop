import { ProductOption } from './product-option';

export class ProductArticle {
  id: number;
  article: string;
  title: string;
  price: number;
  actualPrice: number;
  optionsCount: number;
  mainImage: string;
  products: ProductOption[];
  isProductsShown = false;
}
