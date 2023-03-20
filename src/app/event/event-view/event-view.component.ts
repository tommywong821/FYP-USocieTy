import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {NzMessageRef, NzMessageService} from 'ng-zorro-antd/message';
import {first, Subject, switchMap, tap} from 'rxjs';
import {Path} from 'src/app/app-routing.module';
import {EventEnrollmentRecord, EventEnrollmentStatus, EventProperty, PaymentStatus} from 'src/app/model/event';
import {ApiService} from 'src/app/services/api.service';
import {Event} from '../../model/event';

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

  enrollmentRecords: EventEnrollmentRecord[] = [];
  refreshEnrollmentRecords$ = new Subject();

  toBeUpdatedEnrollmentRecords: Record<string, EnrollmentStatus> = {};

  pageIndex = 1;
  pageSize = 15;

  loadingMessage: NzMessageRef | null = null;

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
        tap(() => (this.loadingMessage = this.message.loading('Fetching event details...'))),
        tap(params => (this.eventId = params['eventId'])),
        switchMap(params => this.ApiService.getEvent(params['eventId'])),
        tap(() => this.message.remove(this.loadingMessage?.messageId))
      )
      .subscribe(event => (this.event = event));

    this.refreshEnrollmentRecords$
      .pipe(
        switchMap(() => this.ApiService.getEventEnrollmentRecord(this.eventId, this.pageIndex, this.pageSize)),
        tap(records => console.log(records))
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
      itsc: key,
      studentId: val.studentId,
      paymentStatus: val.paymentStatus,
      enrolledStatus: val.enrolledStatus,
    }));
    this.ApiService.updateEventEnrollmentRecords(this.eventId, records as EventEnrollmentRecord[]);
    this.message.loading('Updating event enrollment records...', {nzDuration: 2000});
    this.refreshEnrollmentRecords$.next({});
    this.toBeUpdatedEnrollmentRecords = {};
  }

  backToEventPage(): void {
    this.router.navigate([Path.Main, Path.Event]);
  }
}
