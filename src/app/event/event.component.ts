import {EventProperty} from './../model/event';
import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {filter, ReplaySubject, Subject, switchMap, tap} from 'rxjs';
import {AuthService} from 'src/app/services/auth.service';
import {Path} from '../app-routing.module';
import {ApiService} from '../services/api.service';
import {Event} from '../model/event';
import {NzMessageRef, NzMessageService} from 'ng-zorro-antd/message';

export const EventTableColumn = [
  {
    title: 'Society',
  },
  {
    title: 'Name',
  },
  {
    title: 'Category',
  },
  {
    title: 'Location',
  },
  {
    title: 'Max Participant',
  },
  {
    title: 'Fee',
  },
  {
    title: 'Apply Deadline',
  },
  {
    title: 'Start Date',
  },
  {
    title: 'End Date',
  },
  {
    title: 'Action',
  },
];

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.scss'],
})
export class EventComponent implements OnInit {
  eventProperties = Object.keys(EventProperty);
  EventTableColumn = EventTableColumn;
  eventTableHeaders = EventTableColumn.map(col => col.title);

  events: Event[] = [];
  eventTotal = 0;
  refreshEvents$ = new Subject();

  enrolledSocieties: string[] = [];

  pageIndex = 1;
  pageSize = 15;

  deleteEvent$ = new Subject();
  deleteEventId$ = new ReplaySubject<string>();

  showModal = false;

  loadingMessage: NzMessageRef | null = null;

  constructor(
    private router: Router,
    private ApiService: ApiService,
    private message: NzMessageService,
    private AuthService: AuthService
  ) {}

  ngOnInit(): void {
    this.AuthService.user$
      .pipe(
        filter(user => !!user),
        tap(user => console.log(user)),
        tap(user => (this.enrolledSocieties = [...user!.enrolledSocieties])),
        switchMap(user => this.ApiService.getEventCount(user!.uuid)),
        tap(eventTotal => (this.eventTotal = eventTotal))
      )
      .subscribe();

    this.refreshEvents$
      .pipe(
        tap(() => (this.loadingMessage = this.message.loading('Fetching events...'))),
        switchMap(() => this.ApiService.getEvents(this.pageIndex, this.pageSize)),
        tap(() => this.message.remove(this.loadingMessage?.messageId))
      )
      .subscribe(event => (this.events = ([] as Event[]).concat(event)));

    this.deleteEvent$
      .pipe(
        switchMap(() => this.deleteEventId$.asObservable()),
        tap(() => this.message.loading('Deleting event...', {nzDuration: 2000})),
        switchMap(eventId => this.ApiService.deleteEvent(eventId))
      )
      .subscribe();

    this.refreshEvents$.next({});
  }

  changePageIndex(): void {
    this.refreshEvents$.next({});
  }

  toggleCreateEvent(): void {
    this.router.navigate([Path.Main, Path.Event, Path.CreateEvent]);
  }

  toggleViewEvent(eventId: string): void {
    this.router.navigate([Path.Main, Path.Event, Path.ViewEvent], {queryParams: {eventId: eventId}});
  }

  toggleDeleteEvent(eventId: string): void {
    this.showModal = true;
    this.deleteEventId$.next(eventId);
  }

  confirmEventDeletion(): void {
    this.showModal = false;
    this.deleteEvent$.next({});
    setTimeout(() => {
      this.refreshEvents$.next({});
    }, 2000);
  }

  cancelEventDeletion(): void {
    this.showModal = false;
  }
}
