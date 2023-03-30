import {EventAction, EventProperty} from './../model/event';
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

  messages: Record<EventAction, NzMessageRef | null> = {
    [EventAction.Create]: null,
    [EventAction.Update]: null,
    [EventAction.Fetch]: null,
    [EventAction.Delete]: null,
  };

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
        tap(user => (this.enrolledSocieties = [...user!.enrolledSocieties])),
        switchMap(user => this.ApiService.getEventCount(user!.uuid)),
        tap(eventTotal => (this.eventTotal = eventTotal))
      )
      .subscribe();

    this.refreshEvents$
      .pipe(
        tap(() => (this.messages[EventAction.Fetch] = this.message.loading('Fetching events...'))),
        switchMap(() => this.AuthService.user$),
        switchMap(user => this.ApiService.getEvents(user!.uuid, this.pageIndex, this.pageSize)),
        tap(() => this.message.remove(this.messages[EventAction.Fetch]!.messageId)),
        tap(event => (this.events = ([] as Event[]).concat(event)))
      )
      .subscribe({
        error: err => {
          this.message.error('Unable to fetch events', {nzDuration: 2000});
          this.message.remove(this.messages[EventAction.Fetch]?.messageId);
        },
      });

    this.deleteEvent$
      .pipe(
        switchMap(() => this.deleteEventId$.asObservable()),
        tap(() => (this.messages![EventAction.Delete] = this.message.loading('Deleting event...'))),
        switchMap(eventId => this.ApiService.deleteEvent(eventId)),
        tap(() => this.message.remove(this.messages[EventAction.Delete]!.messageId)),
        tap(() => this.message.success('Successfully deleted event')),
        tap(() => this.refreshEvents$.next({}))
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
  }

  cancelEventDeletion(): void {
    this.showModal = false;
  }
}
