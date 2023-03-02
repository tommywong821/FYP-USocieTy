import {Component, OnInit} from '@angular/core';
import {Router, Params} from '@angular/router';
import {BehaviorSubject, map, Subject, switchMap} from 'rxjs';
import {Path} from '../app-routing.module';
import {ApiService} from '../services/api.service';
import {Event} from '../model/event';

export enum EventTableColumn {
  name = 'Name',
  maxParticipation = 'Max Participation',
  applyDeadline = 'Apply Deadline',
  location = 'Location',
  startDate = 'Start Date',
  endDate = 'End Date',
  category = 'Category',
  // description = 'Description',
  fee = 'Fee',
}

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.scss'],
})
export class EventComponent implements OnInit {
  EventTableColumn = EventTableColumn;

  events: Event[] = [];
  refreshEvents$ = new Subject();

  pageIndex = 1;
  pageSize = 15;

  constructor(private router: Router, private ApiService: ApiService) {}

  ngOnInit(): void {
    this.refreshEvents$
      .pipe(switchMap(() => this.ApiService.getEvents(this.pageIndex, this.pageSize)))
      .subscribe(event => (this.events = ([] as Event[]).concat(event)));
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

  deleteEvent(eventId: string): void {
    this.ApiService.deleteEvent(eventId);
    this.refreshEvents$.next({});
  }
}
