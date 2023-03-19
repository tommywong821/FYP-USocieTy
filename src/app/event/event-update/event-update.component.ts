import {AuthService} from 'src/app/services/auth.service';
import {ApiService} from './../../services/api.service';
import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {NzMessageService} from 'ng-zorro-antd/message';
import {NzUploadChangeParam} from 'ng-zorro-antd/upload';
import {Subject, filter, zip, takeUntil, tap, map, switchMap, first, finalize} from 'rxjs';
import {EventCategory} from 'src/app/model/event';
import {getUpdateEventRequest, convertFiletoBase64, convertFormDataToEvent} from 'src/util/event.util';
import {Event} from '../../model/event';
import {ActivatedRoute, Router} from '@angular/router';
import {Path} from 'src/app/app-routing.module';

export enum UpdateEventFormFields {
  EventTitle = 'eventTitle',
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

  enrolledSocieties: string[] = [];

  eventId = '';
  event$ = new Subject<Event>();

  destroy$ = new Subject<void>();

  loadingMessage: string | undefined;

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
          tap(params => (this.eventId = params['eventId'])),
          switchMap(params => this.ApiService.getEvent(params['eventId']))
        )
        .subscribe(
          event =>
            (this.updateEventForm = this.formBuilder.group({
              eventTitle: [event.name, [Validators.required]],
              location: [event.location, [Validators.required]],
              society: [event.society, [Validators.required]],
              maxParticipation: [event.maxParticipation, [Validators.required]],
              applyDeadline: [event.applyDeadline, [Validators.required]],
              date: [[event.startDate, event.endDate], [Validators.required]],
              category: [event.category, [Validators.required]],
              description: [event.description, [Validators.required]],
              fee: [event.fee, [Validators.required]],
            }))
        );

      this.AuthService.user$
        .pipe(filter(user => !!user))
        .subscribe(user => (this.enrolledSocieties = [...user!.enrolledSocieties]));

      zip([this.event$, this.AuthService.user$])
        .pipe(
          takeUntil(this.destroy$),
          tap(() => (this.isProcessing = true)),
          filter(([event, user]) => !!user),
          map(([event, user]) => getUpdateEventRequest(this.eventId, event, this.updateEventForm.value.society, user!)),
          finalize(() => this.router.navigate([Path.Main, Path.Event]))
        )
        .subscribe(request => this.ApiService.updateEvent(request));
    }
  }

  ngOnDestroy(): void {
    this.destroy$.next();
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

    this.loadingMessage = this.message.loading('Request in progress...', {nzDuration: 0}).messageId;
    convertFiletoBase64(this.pictureFile)
      .pipe(map(fileBuffer => convertFormDataToEvent({...this.updateEventForm.value, poster: fileBuffer})))
      .subscribe(event => this.event$.next(event));
  }

  saveFileBuffer({file}: NzUploadChangeParam): void {
    this.pictureFile = file.originFileObj;
  }

  backToEventViewPage(): void {
    this.router.navigate([Path.Main, Path.Event, Path.ViewEvent], {queryParams: {eventId: this.eventId}});
  }
}
