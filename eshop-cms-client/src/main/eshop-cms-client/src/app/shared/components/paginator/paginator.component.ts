import { Component, OnInit, Input, OnChanges, SimpleChanges, SimpleChange, Output, EventEmitter } from '@angular/core';
import { Page } from '../../model/page';

@Component({
  selector: 'app-paginator',
  templateUrl: './paginator.component.html',
  styleUrls: ['./paginator.component.css']
})
export class PaginatorComponent implements OnInit, OnChanges {

  @Input()
  page: Page<any>;

  @Input()
  pageSizes = [20, 35, 50];
  pageNumbers: number[];
  currentPageNumber: number;

  @Output()
  paginatorEvent: EventEmitter<{pageNumber: number, pageSize: number}> = new EventEmitter();

  isFirstEnabled = false;
  isPreviousEnabled = false;
  isNextEnabled = false;
  isLastEnabled = false;

  constructor() { }

  ngOnInit() {}

  ngOnChanges(changes: SimpleChanges) {
    let change: SimpleChange;
    change = changes['page'];
    if (change && change.currentValue) {
      const page: Page<any> = change.currentValue;
      this.pageNumbers = Array.apply(null, {length: page.totalPages}).map((value, index) => index + 1);
      this.currentPageNumber = page.pageNumber;
      this.isFirstEnabled = this.currentPageNumber > 1;
      this.isPreviousEnabled = this.currentPageNumber > 1;
      this.isNextEnabled = this.currentPageNumber < page.totalPages;
      this.isLastEnabled = this.currentPageNumber < page.totalPages;
    }
  }

  onPageSelect(pageNumber: number) {
    this.paginatorEvent.emit({pageNumber: pageNumber, pageSize: this.page.pageSize});
  }

  onPageSizeChanged(pageSize: number) {
    this.paginatorEvent.emit({pageNumber: this.currentPageNumber, pageSize: pageSize});
  }
}
