import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {first, Subject, switchMap, tap} from 'rxjs';
import {Path} from 'src/app/app-routing.module';
import {
  EventCategory,
  EventEnrollmentRecord,
  EventEnrollmentStatus,
  EventProperty,
  PaymentStatus,
} from 'src/app/model/event';
import {ApiService} from 'src/app/services/api.service';
import {Event} from '../../model/event';

@Component({
  selector: 'app-event-view',
  templateUrl: './event-view.component.html',
  styleUrls: ['./event-view.component.scss'],
})
export class EventViewComponent implements OnInit {
  EventProperty = EventProperty;
  EventEnrollmentStatus = EventEnrollmentStatus;

  enrollmentTableColumn = [{title: 'itsc'}, {title: 'payment status'}, {title: 'enrollment status'}];

  eventId = '';
  // event?: Event = undefined;
  event?: Event = {
    id: '',
    name: 'Ocamp',
    poster: '',
    maxParticipation: 120,
    applyDeadline: new Date(),
    location: 'HKUST',
    startDate: new Date(),
    endDate: new Date(),
    category: EventCategory.OrientationCamp,
    description: 'This is the best Ocamp! hahaha',
    fee: 100,
    society: 'HKUSTSU',
  };

  enrollmentRecords: EventEnrollmentRecord[] = [
    {
      itsc: 'cywongch',
      paymentStatus: PaymentStatus.Paid,
      eventEnrollmentStatus: EventEnrollmentStatus.Enrolled,
    },
  ];
  refreshEnrollmentRecords$ = new Subject();

  pageIndex = 1;
  pageSize = 15;

  constructor(private router: Router, private ApiService: ApiService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParams
      .pipe(
        first(),
        tap(params => (this.eventId = params['eventId'])),
        switchMap(params => this.ApiService.getEvent(params['eventId']))
      )
      .subscribe(event => (this.event = event));

    // TODO
    // this.refreshEnrollmentRecords$
    //   .pipe(switchMap(() => this.ApiService.(this.pageIndex, this.pageSize)))
    //   .subscribe(event => (this.events = ([] as Event[]).concat(event)));
  }

  changePageIndex(): void {
    this.refreshEnrollmentRecords$.next({});
  }

  toggleUpdateEvent(): void {
    this.router.navigate([Path.Main, Path.Event, Path.ViewEvent], {queryParams: {eventId: this.eventId}});
  }

  updateEnrollmentStatusOfStudent(studentId: string, status: string): void {
    this.ApiService.updateEnrollmentStatus(this.eventId, studentId, status);
  }
}
