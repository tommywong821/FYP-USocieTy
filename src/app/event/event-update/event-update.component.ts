import {AuthService} from 'src/app/services/auth.service';
import {ApiService} from './../../services/api.service';
import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {NzMessageRef, NzMessageService} from 'ng-zorro-antd/message';
import {NzUploadChangeParam, NzUploadFile} from 'ng-zorro-antd/upload';
import {Subject, filter, tap, switchMap, first, catchError, of} from 'rxjs';
import {EventCategory} from 'src/app/model/event';
import {convertFormDataToEvent, getPictureNameFromUrl} from 'src/util/event.util';
import {Event} from '../../model/event';
import {ActivatedRoute, Router} from '@angular/router';
import {Path} from 'src/app/app-routing.module';
import {HttpErrorResponse} from '@angular/common/http';

export enum UpdateEventFormFields {
  Name = 'name',
  Location = 'location',
  Society = 'society',
  MaxParticipation = 'maxParticipation',
  ApplyDeadline = 'applyDeadline',
  Date = 'date',
  Category = 'category',
  Description = 'description',
  Fee = 'fee',
}

@Component({
  selector: 'app-event-update',
  templateUrl: './event-update.component.html',
  styleUrls: ['./event-update.component.scss'],
})
export class EventUpdateComponent implements OnInit {
  UpdateEventFormFields = UpdateEventFormFields;
  EventCategory = EventCategory;

  updateEventForm!: FormGroup;
  pictureFile: File | undefined;

  fileList: NzUploadFile[] = [];

  roles: string[] = [];

  eventId = '';
  event$ = new Subject<Event>();

  destroy$ = new Subject<void>();

  loadingMessage: NzMessageRef | null = null;

  isProcessing = false;

  constructor(
    private ApiService: ApiService,
    private formBuilder: FormBuilder,
    private message: NzMessageService,
    private AuthService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    {
      this.route.queryParams
        .pipe(
          first(),
          tap(() => (this.loadingMessage = this.message.loading('Fetching event details...'))),
          tap(params => (this.eventId = params['eventId'])),
          switchMap(params => this.ApiService.getEvent(params['eventId'])),
          tap(
            event =>
              (this.fileList = [
                {
                  uid: '1',
                  name: getPictureNameFromUrl(event.poster),
                  status: 'done',
                  url: event.poster,
                  thumbUrl: event.poster,
                },
              ])
          ),
          tap(event => console.log(event)),
          tap(event => this.loadDataToUpdateEventForm(event)),
          tap(() => this.message.remove(this.loadingMessage?.messageId)),
          catchError((err: HttpErrorResponse) => of(err)),
          tap(err => console.error(err)),
          tap(() => {
            this.message.error('Unable to fetch event details', {nzDuration: 2000});
          })
        )
        .subscribe();

      this.AuthService.user$.pipe(filter(user => !!user)).subscribe(user => (this.roles = [...user!.roles]));

      this.event$
        .pipe(
          tap(() => (this.loadingMessage = this.message.loading('Updating event...'))),
          switchMap(event => this.ApiService.updateEvent(event, this.updateEventForm.value.society, this.pictureFile)),
          tap(() => this.message.remove(this.loadingMessage?.messageId)),
          tap(() => this.message.success('Successfully created event', {nzDuration: 2000})),
          tap(() => this.backToEventViewPage()),
          catchError((err: HttpErrorResponse) => of(err)),
          tap(err => console.error(err)),
          tap(() => {
            this.message.error('Unable to update event details', {nzDuration: 2000});
          })
        )
        .subscribe();
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
  }

  loadDataToUpdateEventForm(event: Event): void {
    this.updateEventForm = this.formBuilder.group({
      name: [event.name, [Validators.required]],
      location: [event.location, [Validators.required]],
      society: [event.society, [Validators.required]],
      maxParticipation: [event.maxParticipation, [Validators.required]],
      applyDeadline: [event.applyDeadline, [Validators.required]],
      date: [[event.startDate, event.endDate], [Validators.required]],
      category: [event.category, [Validators.required]],
      description: [event.description, [Validators.required]],
      fee: [event.fee, [Validators.required]],
    });
  }

  updateEvent(): void {
    if (!this.updateEventForm.valid || !this.pictureFile) {
      this.message.error('Field(s) are missing');
      Object.values(this.updateEventForm.controls).forEach(control => {
        if (control.invalid) {
          control.markAsDirty();
          control.updateValueAndValidity({onlySelf: true});
        }
      });
      return;
    }

    this.isProcessing = true;
    this.event$.next(convertFormDataToEvent({...this.updateEventForm.value}));
  }

  saveFileBuffer({file}: NzUploadChangeParam): void {
    this.pictureFile = file.originFileObj;
  }

  backToEventViewPage(): void {
    this.router.navigate([Path.Main, Path.Event, Path.ViewEvent], {queryParams: {eventId: this.eventId}});
  }
}
