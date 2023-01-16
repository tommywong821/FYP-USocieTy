import {ApiService} from './../../services/api.service';
import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NzMessageService} from 'ng-zorro-antd/message';
import {NzUploadChangeParam, NzUploadFile} from 'ng-zorro-antd/upload';

export enum CreateEventFormFields {
  EventTitle = 'eventTitle',
  Location = 'location',
  MaxParticipations = 'maxParticipations',
  Date = 'date',
  ApplyDeadline = 'applyDeadline',
}

@Component({
  selector: 'app-event-create',
  templateUrl: './event-create.component.html',
  styleUrls: ['./event-create.component.scss'],
})
export class EventCreateComponent implements OnInit {
  CreateEventFormFields = CreateEventFormFields;
  createEventForm!: FormGroup;
  pictureFile!: NzUploadFile;

  isProcessing = false;

  constructor(private ApiService: ApiService, private formBuilder: FormBuilder, private message: NzMessageService) {}

  ngOnInit(): void {
    this.createEventForm = this.formBuilder.group({
      eventTitle: [''],
      location: [''],
      maxParticipations: [''],
      date: [''],
      applyDeadline: [''],
    });
  }

  createEvent(): void {
    if (!this.pictureFile.originFileObj) {
      this.message.error('Error');
      return;
    }

    const fileReader = new FileReader();
    const fileBuffer = fileReader.readAsBinaryString(this.pictureFile.originFileObj);

    this.isProcessing = true;
    const onLoading = this.message.loading('Request in progress...', {nzDuration: 0}).messageId;

    // TODO error response handling
    this.ApiService.createEvent({...this.createEventForm.value, poster: fileBuffer}).subscribe();

    this.isProcessing = false;
    this.message.remove(onLoading);
  }

  uploadPicture({file}: NzUploadChangeParam): void {
    this.pictureFile = file;
  }
}
