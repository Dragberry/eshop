import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { PaginatorComponent } from './components/table/paginator/paginator.component';
import { BooleanBadgeDirective } from './directives/boolean-badge/boolean-badge.directive';
import { TableActionColumnComponent } from './components/table/table-action-column/table-action-column.component';
import { BsDropdownModule } from 'ngx-bootstrap/dropdown';
import { TranslateModule } from '@ngx-translate/core';
import { TableListFilterComponent } from './components/table/table-list-filter/table-list-filter.component';
import { FormsModule } from '@angular/forms';

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    BsDropdownModule,
    TranslateModule
  ],
  declarations: [
    BooleanBadgeDirective,
    PaginatorComponent,
    TableActionColumnComponent,
    TableListFilterComponent
  ],
  exports: [
    BsDropdownModule,
    BooleanBadgeDirective,
    PaginatorComponent,
    TableActionColumnComponent,
    TableListFilterComponent
  ],
  providers: []
})
export class SharedModule {

  constructor() {}

}
