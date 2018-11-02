import { Input } from '@angular/core';

export abstract class TableFilter {

  @Input()
  columnId: string;

  constructor() { }

  abstract getSelectedValues(): {name: string, values: string[]}[];

  abstract reset(): void;

  abstract clearAll(): void;
}
