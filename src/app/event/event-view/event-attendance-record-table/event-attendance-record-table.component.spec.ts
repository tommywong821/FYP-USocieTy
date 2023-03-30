import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EventAttendanceRecordTableComponent } from './event-attendance-record-table.component';

describe('EventAttendanceRecordTableComponent', () => {
  let component: EventAttendanceRecordTableComponent;
  let fixture: ComponentFixture<EventAttendanceRecordTableComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EventAttendanceRecordTableComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(EventAttendanceRecordTableComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
