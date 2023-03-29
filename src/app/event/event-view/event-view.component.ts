import {EventAction} from './../../model/event';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {NzMessageRef, NzMessageService} from 'ng-zorro-antd/message';
import {catchError, first, map, of, Subject, switchMap, tap} from 'rxjs';
import {Path} from 'src/app/app-routing.module';
import {
  EventEnrollmentRecord,
  EventEnrollmentStatus,
  EventProperty,
  PaymentStatus,
  UpdateEventEnrollmentRecordPayload,
} from 'src/app/model/event';
import {ApiService} from 'src/app/services/api.service';
import {Event} from '../../model/event';
import {HttpErrorResponse} from '@angular/common/http';

export type EnrollmentStatus = Partial<EventEnrollmentRecord>;

@Component({
  selector: 'app-event-view',
  templateUrl: './event-view.component.html',
  styleUrls: ['./event-view.component.scss'],
})
export class EventViewComponent implements OnInit {
  objectKeys = Object.keys;
  EventProperty = EventProperty;
  EventEnrollmentStatus = EventEnrollmentStatus;
  PaymentStatus = PaymentStatus;

  enrollmentTableColumn = [{title: 'Itsc'}, {title: 'Payment Status'}, {title: 'Enrollment Status'}];

  eventId = '';
  event?: Event = undefined;
  recordTotal = 0;

  enrollmentRecords: EventEnrollmentRecord[] = [];
  refreshEnrollmentRecords$ = new Subject();

  toBeUpdatedEnrollmentRecords: Record<string, EnrollmentStatus> = {};

  updateEnrollmentRecord$ = new Subject<UpdateEventEnrollmentRecordPayload[]>();

  pageIndex = 1;
  pageSize = 15;

  messages: Record<EventAction, NzMessageRef | null> = {
    [EventAction.Create]: null,
    [EventAction.Update]: null,
    [EventAction.Fetch]: null,
    [EventAction.Delete]: null,
  };

  constructor(
    private router: Router,
    private ApiService: ApiService,
    private route: ActivatedRoute,
    private message: NzMessageService
  ) {}

  ngOnInit(): void {
    this.route.queryParams
      .pipe(
        first(),
        tap(() => (this.messages[EventAction.Fetch] = this.message.loading('Fetching event details...'))),
        tap(params => (this.eventId = params['eventId'])),
        switchMap(params => this.ApiService.getEvent(params['eventId'])),
        tap(event => (this.event = event)),
        switchMap(event => this.ApiService.getEventEnrollmentRecordCount(event.id!)),
        tap(recordCount => (this.recordTotal = recordCount)),
        tap(() => this.message.remove(this.messages[EventAction.Fetch]!.messageId))
      )
      .subscribe({
        error: err => {
          this.message.remove(this.messages[EventAction.Fetch]?.messageId);
          this.message.error('Unable to fetch event enrollment records', {nzDuration: 2000});
        },
      });

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
        switchMap(() => this.ApiService.getEventEnrollmentRecord(this.eventId, this.pageIndex, this.pageSize)),
        map(
          records =>
            records.map(record => ({
              ...record,
              paymentStatus: record.paymentStatus.toUpperCase(),
              enrolledStatus: record.enrolledStatus.toLocaleUpperCase(),
            })) as EventEnrollmentRecord[]
        )
      )
      .subscribe(record => (this.enrollmentRecords = ([] as EventEnrollmentRecord[]).concat(record)));

    this.refreshEnrollmentRecords$.next({});
  }

  changePageIndex(): void {
    this.refreshEnrollmentRecords$.next({});
  }

  toggleUpdateEvent(): void {
    this.router.navigate([Path.Main, Path.Event, Path.UpdateEvent], {queryParams: {eventId: this.eventId}});
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

  backToEventPage(): void {
    this.router.navigate([Path.Main, Path.Event]);
  }
}
