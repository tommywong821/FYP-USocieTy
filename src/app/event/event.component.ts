import {Component, OnInit} from '@angular/core';
import {Router, Params} from '@angular/router';
import {BehaviorSubject, map, switchMap} from 'rxjs';
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

  private _pageIndex = 1;

  get pageIndex(): number {
    return this._pageIndex;
  }

  set pageIndex(value: number) {
    this._pageIndex = value;
    this.pageIndex$.next(value);
  }

  pageIndex$ = new BehaviorSubject<number>(this.pageIndex);
  pageSize = 15;

  constructor(private router: Router, private ApiService: ApiService) {}

  ngOnInit(): void {
    this.pageIndex$
      .pipe(switchMap(pageIndex => this.ApiService.getEvents(pageIndex, this.pageSize)))
      .subscribe(events => (this.events = events));
  }

  toggleCreateEvent(): void {
    this.router.navigate([Path.Main, Path.Event, Path.CreateEvent]);
  }

  toggleViewEvent(eventId: string): void {
    this.router.navigate([Path.Main, Path.Event, Path.ViewEvent], {queryParams: {eventId: eventId}});
  }

  deleteEvent(eventId: string): void {
    // TODO refresh after deletion
    this.ApiService.deleteEvent(eventId);
  }
}
