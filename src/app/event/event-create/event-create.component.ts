import {AuthService} from 'src/app/services/auth.service';
import {ApiService} from './../../services/api.service';
import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService} from 'ng-zorro-antd/message';
import {NzUploadChangeParam} from 'ng-zorro-antd/upload';
import {EventCategory} from 'src/app/model/event';
import {convertFiletoBase64, convertFormDataToEvent, getCreateEventRequest} from 'src/util/event.util';
import {filter, map, Subject, switchMap, tap, zip, takeUntil} from 'rxjs';
import {Event} from '../../model/event';

export enum CreateEventFormFields {
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
  selector: 'app-event-create',
  templateUrl: './event-create.component.html',
  styleUrls: ['./event-create.component.scss'],
})
export class EventCreateComponent implements OnInit {
  CreateEventFormFields = CreateEventFormFields;
  EventCategory = EventCategory;

  createEventForm!: FormGroup;
  pictureFile: File | undefined;

  enrolledSocieties: string[] = [];

  event$ = new Subject<Event>();

  destroy$ = new Subject<void>();

  loadingMessage: string | undefined;

  isProcessing = false;

  constructor(
    private ApiService: ApiService,
    private formBuilder: FormBuilder,
    private message: NzMessageService,
    private AuthService: AuthService
  ) {}

  ngOnInit(): void {
    this.createEventForm = this.formBuilder.group({
      eventTitle: ['', [Validators.required]],
      location: ['', [Validators.required]],
      society: ['', [Validators.required]],
      maxParticipation: ['', [Validators.required]],
      applyDeadline: ['', [Validators.required]],
      date: ['', [Validators.required]],
      category: ['', [Validators.required]],
      description: ['', [Validators.required]],
      fee: ['', [Validators.required]],
    });

    this.AuthService.user$
      .pipe(filter(user => !!user))
      .subscribe(user => (this.enrolledSocieties = [...user!.enrolledSocieties]));

    zip([this.event$, this.AuthService.user$])
      .pipe(
        takeUntil(this.destroy$),
        tap(() => (this.isProcessing = true)),
        filter(([event, user]) => !!user),
        map(([event, user]) => getCreateEventRequest(event, this.createEventForm.value.society, user!)),
        switchMap(request => this.ApiService.call(request))
      )
      .subscribe(res => {
        this.isProcessing = false;
        this.message.remove(this.loadingMessage);
      });
  }

  ngOnDestroy(): void {
    this.destroy$.next();
  }

  createEvent(): void {
    if (!this.createEventForm.valid || !this.pictureFile) {
      this.message.error('Field(s) are missing');
      Object.values(this.createEventForm.controls).forEach(control => {
        if (control.invalid) {
          control.markAsDirty();
          control.updateValueAndValidity({onlySelf: true});
        }
      });
      return;
    }

    this.loadingMessage = this.message.loading('Request in progress...', {nzDuration: 0}).messageId;
    convertFiletoBase64(this.pictureFile)
      .pipe(map(fileBuffer => convertFormDataToEvent({...this.createEventForm.value, poster: fileBuffer})))
      .subscribe(event => this.event$.next(event));
  }

  saveFileBuffer({file}: NzUploadChangeParam): void {
    this.pictureFile = file.originFileObj;
  }
}
