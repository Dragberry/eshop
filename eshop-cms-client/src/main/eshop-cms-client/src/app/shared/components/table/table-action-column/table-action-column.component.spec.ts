import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TableActionColumnComponent } from './table-action-column.component';

describe('TableActionColumnComponent', () => {
  let component: TableActionColumnComponent;
  let fixture: ComponentFixture<TableActionColumnComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TableActionColumnComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TableActionColumnComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
