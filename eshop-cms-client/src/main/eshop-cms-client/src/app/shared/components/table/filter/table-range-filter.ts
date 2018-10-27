import { Component, Input } from '@angular/core';
import { TableFilter } from './table-filter';

export abstract class TableRangeFilter<T> extends TableFilter {

  protected sourceFrom: T;
  protected sourceTo: T;

  from: T;
  to: T;

  protected abstract toString(value: T): string;

  getSelectedValues(): {name: string, values: string[]}[] {
    this.sourceFrom = this.from;
    this.sourceTo = this.to;
    return [
      {name: `from[${this.columnId}]`, values: [this.toString(this.from)]},
      {name: `to[${this.columnId}]`, values: [this.toString(this.to)]}
    ];
  }

  reset(): void {
    this.from = this.sourceFrom;
    this.to = this.sourceTo;
  }

  clearAll(): void {
    this.from = null;
    this.to = null;
  }
}
