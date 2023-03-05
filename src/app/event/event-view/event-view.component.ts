import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {first, Subject, switchMap, tap} from 'rxjs';
import {Path} from 'src/app/app-routing.module';
import {ApiService} from 'src/app/services/api.service';

export const enrollmentTableColumn = [{title: 'itsc'}, {title: 'payment status'}, {title: 'enrollment status'}];
@Component({
  selector: 'app-event-view',
  templateUrl: './event-view.component.html',
  styleUrls: ['./event-view.component.scss'],
})
export class EventViewComponent implements OnInit {
  enrollmentTableColumn = enrollmentTableColumn;

  eventId = '';

  enrollmentRecords = [];
  refreshEnrollmentRecords$ = new Subject();

  pageIndex = 1;
  pageSize = 15;

  constructor(private router: Router, private ApiService: ApiService, private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.queryParams.pipe(
      first(),
      tap(params => (this.eventId = params['eventId'])),
      switchMap(params => this.ApiService.getEvent(params['eventId']))
    );

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
