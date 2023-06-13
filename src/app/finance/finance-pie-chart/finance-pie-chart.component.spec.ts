import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinancePieChartComponent } from './finance-pie-chart.component';

describe('FinancePieChartComponent', () => {
  let component: FinancePieChartComponent;
  let fixture: ComponentFixture<FinancePieChartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FinancePieChartComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FinancePieChartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
