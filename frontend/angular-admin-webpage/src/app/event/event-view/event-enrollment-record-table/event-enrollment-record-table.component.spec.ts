import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventEnrollmentRecordTableComponent } from './event-enrollment-record-table.component';

describe('EventEnrollmentRecordTableComponent', () => {
  let component: EventEnrollmentRecordTableComponent;
  let fixture: ComponentFixture<EventEnrollmentRecordTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EventEnrollmentRecordTableComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EventEnrollmentRecordTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
