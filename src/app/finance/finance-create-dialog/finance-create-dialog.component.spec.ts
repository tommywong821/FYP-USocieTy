import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinanceCreateDialogComponent } from './finance-create-dialog.component';

describe('FinanceCreateDialogComponent', () => {
  let component: FinanceCreateDialogComponent;
  let fixture: ComponentFixture<FinanceCreateDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FinanceCreateDialogComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FinanceCreateDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
