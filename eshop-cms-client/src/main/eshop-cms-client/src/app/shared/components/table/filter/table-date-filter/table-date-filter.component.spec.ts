import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { TableDateFilterComponent } from './table-date-filter.component';

describe('TableDateFilterComponent', () => {
  let component: TableDateFilterComponent;
  let fixture: ComponentFixture<TableDateFilterComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ TableDateFilterComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TableDateFilterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
