import {ComponentFixture, TestBed} from '@angular/core/testing';

import {FinanceCreateComponent} from './finance-create.component';

describe('FinanceCreateDialogComponent', () => {
  let component: FinanceCreateComponent;
  let fixture: ComponentFixture<FinanceCreateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FinanceCreateComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FinanceCreateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
