import {EventProperty} from './../model/event';
import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {ReplaySubject, Subject, switchMap} from 'rxjs';
import {Path} from '../app-routing.module';
import {ApiService} from '../services/api.service';
import {Event, EventCategory} from '../model/event';

export const EventTableColumn = [
  {
    title: 'society',
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

  events: Event[] = [
    {
      id: '',
      name: 'Ocamp',
      poster: '',
      maxParticipation: 120,
      applyDeadline: new Date(),
      location: 'HKUST',
      startDate: new Date(),
      endDate: new Date(),
      category: EventCategory.OrientationCamp,
      description: '',
      fee: 100,
      society: 'HKUSTSU',
    },
  ];
  refreshEvents$ = new Subject();

  pageIndex = 1;
  pageSize = 15;

  deleteEvent$ = new Subject();
  deleteEventId$ = new ReplaySubject<string>();

  showModal = false;

  constructor(private router: Router, private ApiService: ApiService) {}

  ngOnInit(): void {
    this.refreshEvents$
      .pipe(switchMap(() => this.ApiService.getEvents(this.pageIndex, this.pageSize)))
      .subscribe(event => (this.events = ([] as Event[]).concat(event)));

    this.deleteEvent$
      .pipe(switchMap(() => this.deleteEventId$.asObservable()))
      .subscribe(eventId => this.ApiService.deleteEvent(eventId));
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
    this.refreshEvents$.next({});
  }

  cancelEventDeletion(): void {
    this.showModal = false;
  }
}
