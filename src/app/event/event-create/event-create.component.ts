import {ApiService} from './../../services/api.service';
import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService} from 'ng-zorro-antd/message';
import {NzUploadChangeParam, NzUploadFile} from 'ng-zorro-antd/upload';
import {EventCategory} from 'src/app/model/event';
import {convertFormDataToEvent} from 'src/util/event.util';

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
  pictureFile!: NzUploadFile;

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
  }

  createEvent(): void {
    if (!this.pictureFile) {
      this.message.error('Cover photo is required');
      return;
    }

    // this.isProcessing = true;
    // const onLoading = this.message.loading('Request in progress...', {nzDuration: 0}).messageId;

    // TODO error response handling
    console.log(
      convertFormDataToEvent({...this.createEventForm.value, poster: this.pictureFile.originFileObj?.arrayBuffer})
    );
    // this.ApiService.createEvent(
    //   convertFormDataToEvent({...this.createEventForm.value, poster: this.pictureFile.originFileObj?.arrayBuffer})
    // ).subscribe();

    // this.isProcessing = false;
    // this.message.remove(onLoading);
  }

  uploadPicture({file}: NzUploadChangeParam): void {
    this.pictureFile = file;
  }
}
