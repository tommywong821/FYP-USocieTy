import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {Path} from '../app-routing.module';

@Component({
  selector: 'app-event',
  templateUrl: './event.component.html',
  styleUrls: ['./event.component.scss'],
})
export class EventComponent implements OnInit {
  constructor(private router: Router) {}

  ngOnInit(): void {
    // TODO fetch all events for enrolled society, list to table
  }

  toggleCreateEvent(): void {
    console.log('triggered');

    this.router.navigate([Path.Main, Path.Event, Path.CreateEvent]);
  }

  toggleUpdateEvent(eventId: string): void {
    this.router.navigate([Path.Main, Path.Event, Path.UpdateEvent], {queryParams: {id: eventId}});
  }
}
