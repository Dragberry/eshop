import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TableNumericFilterComponent } from './table-numeric-filter.component';

describe('TableNumericFilterComponent', () => {
  let component: TableNumericFilterComponent;
  let fixture: ComponentFixture<TableNumericFilterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TableNumericFilterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TableNumericFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
