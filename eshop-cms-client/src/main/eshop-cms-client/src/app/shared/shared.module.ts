import { CommonModule } from '@angular/common';
import { NgModule } from '@angular/core';
import { PaginatorComponent } from './components/paginator/paginator.component';
import { BooleanBadgeDirective } from './directives/boolean-badge/boolean-badge.directive';

@NgModule({
  imports: [
    CommonModule
  ],
  declarations: [
    BooleanBadgeDirective,
    PaginatorComponent
  ],
  exports: [
    BooleanBadgeDirective,
    PaginatorComponent
  ],
  providers: []
})
export class SharedModule {

  constructor() {}

}
