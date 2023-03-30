import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NzMessageRef, NzMessageService} from 'ng-zorro-antd/message';
import {Subject, switchMap, tap} from 'rxjs';
import {EventAction, EventAttendance} from 'src/app/model/event';
import {ApiService} from 'src/app/services/api.service';
import {RecordTable} from '../event-view.component';

@Component({
  selector: 'app-event-attendance-record-table',
  templateUrl: './event-attendance-record-table.component.html',
  styleUrls: ['./event-attendance-record-table.component.scss'],
})
export class EventAttendanceRecordTableComponent implements OnInit {
  @Input() eventId!: string;
  @Output() switchTable = new EventEmitter<RecordTable>();

  attendanceTableColumn = [{title: 'Itsc'}, {title: 'Name'}, {title: 'Attended At'}];

  recordTotal = 0;

  pageIndex = 1;
  pageSize = 15;

  attendanceRecords: EventAttendance[] = [];
  refreshAttendanceRecords$ = new Subject();

  messages: Record<EventAction, NzMessageRef | null> = {
    [EventAction.Create]: null,
    [EventAction.Update]: null,
    [EventAction.Fetch]: null,
    [EventAction.Delete]: null,
  };

  constructor(private ApiService: ApiService, private message: NzMessageService) {}

  ngOnInit(): void {
    this.ApiService.getEventAttendanceRecordCount(this.eventId)
      .pipe(tap(total => (this.recordTotal = total)))
      .subscribe();

    this.refreshAttendanceRecords$
      .pipe(
        tap(() => (this.messages[EventAction.Fetch] = this.message.loading('Fetching event attendance records...'))),
        switchMap(() => this.ApiService.getEventAttendanceRecords(this.eventId!, this.pageIndex, this.pageSize)),
        tap(record => (this.attendanceRecords = ([] as EventAttendance[]).concat(record)))
      )
      .subscribe({
        error: err => {
          this.message.remove(this.messages[EventAction.Fetch]?.messageId);
          this.message.error('Unable to fetch event Attendance records', {nzDuration: 2000});
        },
      });

    this.refreshAttendanceRecords$.next({});
  }

  changePageIndex(): void {
    this.refreshAttendanceRecords$.next({});
  }

  toggleSwitchingToEnrollmentTable() {
    this.switchTable.emit(RecordTable.Enrollment);
  }
}
