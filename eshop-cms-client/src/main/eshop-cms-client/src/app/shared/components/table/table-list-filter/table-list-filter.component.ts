import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-table-list-filter',
  templateUrl: './table-list-filter.component.html',
  styleUrls: ['./table-list-filter.component.css']
})
export class TableListFilterComponent implements OnInit {

  @Input()
  columnId: string;

  selectedOptions: {selected: boolean, option: {value: any, name: string}}[];

  constructor() { }

  ngOnInit() { }

  @Input()
  set options(opts: {value: any, name: string}[]) {
    this.selectedOptions = opts.map(opt => {
      return {selected: false, option: opt};
    });
  }

  selectAll(): void {
    this.selectedOptions.forEach(opt => opt.selected = true);
  }

  resetAll(): void {
    this.selectedOptions.forEach(opt => opt.selected = false);
  }
}
