import {AuthService} from 'src/app/services/auth.service';
import {ApiService} from './../../services/api.service';
import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService} from 'ng-zorro-antd/message';
import {NzUploadChangeParam} from 'ng-zorro-antd/upload';
import {EventCategory} from 'src/app/model/event';
import {convertFiletoBase64, convertFormDataToEvent, getCreateEventRequest} from 'src/util/event.util';
import {filter, forkJoin, map, Subject, switchMap, tap, zip} from 'rxjs';
import {Event} from '../../model/event';

export enum CreateEventFormFields {
  EventTitle = 'eventTitle',
  Location = 'location',
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

  event$ = new Subject<Event>();

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
      maxParticipation: ['', [Validators.required]],
      applyDeadline: ['', [Validators.required]],
      date: ['', [Validators.required]],
      category: ['', [Validators.required]],
      description: ['', [Validators.required]],
      fee: ['', [Validators.required]],
    });

    zip([this.event$, this.AuthService.user$])
      .pipe(
        tap(() => (this.isProcessing = true)),
        filter(([event, user]) => !!user),
        map(([event, user]) => getCreateEventRequest(event, user!)),
        switchMap(request => this.ApiService.createEvent(request))
      )
      .subscribe(res => {
        console.log(res);

        this.isProcessing = false;
        this.message.remove(this.loadingMessage);
      });
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
