import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TableListFilterComponent } from './table-list-filter.component';

describe('TableListFilterComponent', () => {
  let component: TableListFilterComponent;
  let fixture: ComponentFixture<TableListFilterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TableListFilterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TableListFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
