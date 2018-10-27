import { Component, Input, Output, EventEmitter } from '@angular/core';
import { Page } from '../../../model/page';
import { PageableEvent } from '../common/pageable-event';

const DEFAULT_PAGE_SIZE_OPTIONS = [20, 35, 50];

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrls: ['./paginator.component.css']
})
export class PaginatorComponent {

  @Input()
  pageSizes = DEFAULT_PAGE_SIZE_OPTIONS;

  pageExists: boolean;
  pageNumbers: number[];
  currentPageNumber: number;
  currentPageSize: number;
  totalPages: number;

  @Output()
  paginatorEvent: EventEmitter<PageableEvent> = new EventEmitter();

  isFirstEnabled = false;
  isPreviousEnabled = false;
  isNextEnabled = false;
  isLastEnabled = false;

  constructor() { }

  @Input()
  set page(page: Page<any>) {
    if (page) {
      this.pageExists = true;
      this.pageNumbers = Array.apply(null, {length: page.totalPages}).map((value, index) => index + 1);
      this.currentPageNumber = page.pageNumber;
      this.currentPageSize = page.pageSize;
      this.totalPages = page.totalPages;
      this.isFirstEnabled = this.currentPageNumber > 1;
      this.isPreviousEnabled = this.currentPageNumber > 1;
      this.isNextEnabled = this.currentPageNumber < page.totalPages;
      this.isLastEnabled = this.currentPageNumber < page.totalPages;
    }
  }

  onPageSelect(pageNumber: number) {
    this.paginatorEvent.emit({pageNumber: pageNumber, pageSize: this.currentPageSize});
  }

  onPageSizeChanged(pageSize: number) {
    this.paginatorEvent.emit({pageNumber: this.currentPageNumber, pageSize: pageSize});
  }
}
