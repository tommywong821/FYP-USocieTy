import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {first, switchMap, tap} from 'rxjs';
import {Path} from 'src/app/app-routing.module';
import {ApiService} from 'src/app/services/api.service';

@Component({
  selector: 'app-event-view',
  templateUrl: './event-view.component.html',
  styleUrls: ['./event-view.component.scss'],
})
export class EventViewComponent implements OnInit {
  constructor(private router: Router, private ApiService: ApiService, private route: ActivatedRoute) {}

  eventId = '';

  ngOnInit(): void {
    this.route.queryParams.pipe(
      first(),
      tap(params => (this.eventId = params['eventId'])),
      switchMap(params => this.ApiService.getEvent(params['eventId']))
    );
  }

  toggleUpdateEvent(eventId: string): void {
    this.router.navigate([Path.Main, Path.Event, Path.ViewEvent], {queryParams: {eventId: eventId}});
  }

  updateEnrollmentStatusOfStudent(studentId: string, status: string): void {
    this.ApiService.updateEnrollmentStatus(this.eventId, studentId, status);
  }
}
