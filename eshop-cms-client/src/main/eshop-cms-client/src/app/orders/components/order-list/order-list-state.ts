import { DataTableState } from './../../../shared/model/data-table-state';
import { NameValue } from './../../../shared/components/table/common/name-value';
import { Order } from '../../model/order';

export class OrderListState {
  dataTableState: DataTableState<Order>;
  paidStatuses: NameValue<boolean>[];
  orderStatuses: NameValue<string>[];
  paymentMethods: NameValue<string>[];
  shippingMethods: NameValue<string>[];
}
