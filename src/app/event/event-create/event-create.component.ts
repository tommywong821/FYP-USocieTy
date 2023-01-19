import {ApiService} from './../../services/api.service';
import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageRef, NzMessageService} from 'ng-zorro-antd/message';
import {NzUploadChangeParam, NzUploadFile} from 'ng-zorro-antd/upload';
import {EventCategory} from 'src/app/model/event';
import {convertFiletoBase64, convertFormDataToEvent} from 'src/util/event.util';
import {buffer, finalize, map, Observable, Subject, switchMap, tap} from 'rxjs';
import {Event} from '../../model/event';

export enum CreateEventFormFields {
  EventTitle = 'eventTitle',
  Location = 'location',
  MaxParticipations = 'maxParticipations',
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

  createEventRequest$ = new Subject<Event>();

  loadingMessage: string | undefined;

  isProcessing = false;

  constructor(private ApiService: ApiService, private formBuilder: FormBuilder, private message: NzMessageService) {}

  ngOnInit(): void {
    this.createEventForm = this.formBuilder.group({
      eventTitle: ['', [Validators.required]],
      location: ['', [Validators.required]],
      maxParticipations: ['', [Validators.required]],
      applyDeadline: ['', [Validators.required]],
      date: ['', [Validators.required]],
      category: ['', [Validators.required]],
      description: ['', [Validators.required]],
      fee: ['', [Validators.required]],
    });

    this.createEventRequest$
      .pipe(
        tap(() => (this.isProcessing = true)),
        switchMap(event => this.ApiService.createEvent(event))
      )
      .subscribe(() => {
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
      .subscribe(event => this.createEventRequest$.next(event));
  }

  saveFileBuffer({file}: NzUploadChangeParam): void {
    this.pictureFile = file.originFileObj;
  }
}
