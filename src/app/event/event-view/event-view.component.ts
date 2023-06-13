import {EventAction} from './../../model/event';
import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {NzMessageRef, NzMessageService} from 'ng-zorro-antd/message';
import {first, switchMap, tap} from 'rxjs';
import {Path} from 'src/app/app-routing.module';
import {EventEnrollmentRecord} from 'src/app/model/event';
import {ApiService} from 'src/app/services/api.service';
import {Event} from '../../model/event';

export type EnrollmentStatus = Partial<EventEnrollmentRecord>;
export enum RecordTable {
  Enrollment = 'ENROLLMENT',
  Attendance = 'ATTENDANCE',
}

@Component({
  selector: 'app-event-view',
  templateUrl: './event-view.component.html',
  styleUrls: ['./event-view.component.scss'],
})
export class EventViewComponent implements OnInit {
  RecordTable = RecordTable;
  eventId = '';
  event?: Event = undefined;
  recordTable: RecordTable = RecordTable.Enrollment;

  messages: Record<EventAction, NzMessageRef | null> = {
    [EventAction.Create]: null,
    [EventAction.Update]: null,
    [EventAction.Fetch]: null,
    [EventAction.Delete]: null,
  };

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
        tap(() => (this.messages[EventAction.Fetch] = this.message.loading('Fetching event details...'))),
        tap(params => (this.eventId = params['eventId'])),
        switchMap(params => this.ApiService.getEvent(params['eventId'])),
        tap(event => (this.event = event)),
        tap(() => this.message.remove(this.messages[EventAction.Fetch]!.messageId))
      )
      .subscribe({
        error: err => {
          this.message.remove(this.messages[EventAction.Fetch]?.messageId);
          this.message.error('Unable to fetch event enrollment records', {nzDuration: 2000});
        },
      });
  }

  toggleUpdateEvent(): void {
    this.router.navigate([Path.Main, Path.Event, Path.UpdateEvent], {queryParams: {eventId: this.eventId}});
  }

  backToEventPage(): void {
    this.router.navigate([Path.Main, Path.Event]);
  }

  switchRecordTable(recordTable: RecordTable): void {
    this.recordTable = recordTable;
  }
}
