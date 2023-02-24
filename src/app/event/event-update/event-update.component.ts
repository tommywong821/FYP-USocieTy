import {AuthService} from 'src/app/services/auth.service';
import {ApiService} from './../../services/api.service';
import {Component, OnInit} from '@angular/core';
import {FormGroup, FormBuilder, Validators} from '@angular/forms';
import {NzMessageService} from 'ng-zorro-antd/message';
import {NzUploadChangeParam} from 'ng-zorro-antd/upload';
import {Subject, filter, zip, takeUntil, tap, map, switchMap, ReplaySubject} from 'rxjs';
import {EventCategory} from 'src/app/model/event';
import {getUpdateEventRequest, convertFiletoBase64, convertFormDataToEvent} from 'src/util/event.util';
import {Event} from '../../model/event';
import {ActivatedRoute} from '@angular/router';

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

  originalEvent$ = new Subject<Event>();

  event$ = new Subject<Event>();

  destroy$ = new Subject<void>();

  loadingMessage: string | undefined;

  isProcessing = false;

  constructor(
    private ApiService: ApiService,
    private formBuilder: FormBuilder,
    private message: NzMessageService,
    private AuthService: AuthService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    {
      this.originalEvent$.subscribe(
        event =>
          (this.updateEventForm = this.formBuilder.group({
            eventTitle: [event.name, [Validators.required]],
            location: [event.location, [Validators.required]],
            society: ['', [Validators.required]], // TODO
            maxParticipation: [event.maxParticipation, [Validators.required]],
            applyDeadline: [event.applyDeadline, [Validators.required]],
            date: [{startDate: event.startDate, endDate: event.endDate}, [Validators.required]],
            category: [event.category, [Validators.required]],
            description: [event.description, [Validators.required]],
            fee: [event.fee, [Validators.required]],
          }))
      );

      this.AuthService.user$
        .pipe(filter(user => !!user))
        .subscribe(user => (this.enrolledSocieties = [...user!.enrolledSocieties]));

      this.route.queryParams
        .pipe(switchMap(params => this.ApiService.getEvent(params['eventId'])))
        .subscribe(event => this.originalEvent$.next(event));

      zip([this.event$, this.AuthService.user$])
        .pipe(
          takeUntil(this.destroy$),
          tap(() => (this.isProcessing = true)),
          filter(([event, user]) => !!user),
          map(([event, user]) => getUpdateEventRequest(event, this.updateEventForm.value.society, user!))
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
}
