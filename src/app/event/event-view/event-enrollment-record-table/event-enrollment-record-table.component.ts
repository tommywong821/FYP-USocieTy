import {RecordTable} from './../event-view.component';
import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NzMessageRef, NzMessageService} from 'ng-zorro-antd/message';
import {Subject, tap, switchMap, map} from 'rxjs';
import {
  UpdateEventEnrollmentRecordPayload,
  EventAction,
  EventEnrollmentRecord,
  PaymentStatus,
  EventEnrollmentStatus,
  EventProperty,
} from 'src/app/model/event';
import {EnrollmentStatus} from '../event-view.component';
import {ApiService} from 'src/app/services/api.service';

@Component({
  selector: 'app-event-enrollment-record-table',
  templateUrl: './event-enrollment-record-table.component.html',
  styleUrls: ['./event-enrollment-record-table.component.scss'],
})
export class EventEnrollmentRecordTableComponent implements OnInit {
  @Input() eventId!: string;
  @Output() switchTable = new EventEmitter<RecordTable>();

  objectKeys = Object.keys;
  EventProperty = EventProperty;
  EventEnrollmentStatus = EventEnrollmentStatus;
  PaymentStatus = PaymentStatus;

  enrollmentTableColumn = [{title: 'Itsc'}, {title: 'Payment Status'}, {title: 'Enrollment Status'}];

  updateEnrollmentRecord$ = new Subject<UpdateEventEnrollmentRecordPayload[]>();
  toBeUpdatedEnrollmentRecords: Record<string, EnrollmentStatus> = {};

  recordTotal = 0;

  pageIndex = 1;
  pageSize = 15;

  enrollmentRecords: EventEnrollmentRecord[] = [];
  refreshEnrollmentRecords$ = new Subject();

  messages: Record<EventAction, NzMessageRef | null> = {
    [EventAction.Create]: null,
    [EventAction.Update]: null,
    [EventAction.Fetch]: null,
    [EventAction.Delete]: null,
  };

  constructor(private ApiService: ApiService, private message: NzMessageService) {}

  ngOnInit(): void {
    this.ApiService.getEventEnrollmentRecordCount(this.eventId).subscribe(total => (this.recordTotal = total));

    this.updateEnrollmentRecord$
      .pipe(
        tap(() => (this.messages[EventAction.Update] = this.message.loading('Updating event enrollment records...'))),
        switchMap(records => this.ApiService.updateEventEnrollmentRecords(this.eventId, records)),
        tap(() => this.message.remove(this.messages[EventAction.Update]!.messageId)),
        tap(() => this.message.success('Successfully updated event enrollment records')),
        tap(() => this.refreshEnrollmentRecords$.next({}))
      )
      .subscribe({
        error: err => {
          this.message.remove(this.messages[EventAction.Fetch]?.messageId);
          this.message.error('Unable to update event enrollment records', {nzDuration: 2000});
        },
      });

    this.refreshEnrollmentRecords$
      .pipe(
        tap(() => (this.messages[EventAction.Fetch] = this.message.loading('Fetching event enrollment records...'))),
        switchMap(() => this.ApiService.getEventEnrollmentRecord(this.eventId!, this.pageIndex, this.pageSize)),
        map(
          records =>
            records.map(record => ({
              ...record,
              paymentStatus: record.paymentStatus.toUpperCase(),
              enrolledStatus: record.enrolledStatus.toLocaleUpperCase(),
            })) as EventEnrollmentRecord[]
        ),
        tap(record => (this.enrollmentRecords = ([] as EventEnrollmentRecord[]).concat(record)))
      )
      .subscribe({
        error: err => {
          this.message.remove(this.messages[EventAction.Fetch]?.messageId);
          this.message.error('Unable to fetch event enrollment records', {nzDuration: 2000});
        },
      });

    this.refreshEnrollmentRecords$.next({});
  }

  changePageIndex(): void {
    this.refreshEnrollmentRecords$.next({});
  }

  recordPaymentStatusChanges(paymentStatus: PaymentStatus, itsc: string): void {
    this.toBeUpdatedEnrollmentRecords[itsc] = this.toBeUpdatedEnrollmentRecords[itsc]
      ? {...this.toBeUpdatedEnrollmentRecords[itsc], paymentStatus}
      : {...this.enrollmentRecords.find(record => record.itsc === itsc)!, paymentStatus};
  }

  recordEnrollmentStatusChanges(enrolledStatus: EventEnrollmentStatus, itsc: string): void {
    this.toBeUpdatedEnrollmentRecords[itsc] = this.toBeUpdatedEnrollmentRecords[itsc]
      ? {...this.toBeUpdatedEnrollmentRecords[itsc], enrolledStatus}
      : {...this.enrollmentRecords.find(record => record.itsc === itsc)!, enrolledStatus};
  }

  updateEnrollmentRecords(): void {
    const records = Object.entries(this.toBeUpdatedEnrollmentRecords).map(([key, val]) => ({
      eventId: this.eventId,
      studentId: val.studentId!,
      paymentStatus: val.paymentStatus!,
      enrolledStatus: val.enrolledStatus!,
    }));
    this.updateEnrollmentRecord$.next(records);
    this.toBeUpdatedEnrollmentRecords = {};
  }

  toggleSwitchingToAttendanceTable() {
    this.switchTable.emit(RecordTable.Attendance);
  }
}
