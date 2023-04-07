import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageRef, NzMessageService} from 'ng-zorro-antd/message';
import {NzUploadChangeParam, NzUploadFile} from 'ng-zorro-antd/upload';
import {filter, Subject, switchMap, tap} from 'rxjs';
import {Path} from 'src/app/app-routing.module';
import {EventCategory} from 'src/app/model/event';
import {AuthService} from 'src/app/services/auth.service';
import {convertFormDataToEvent} from 'src/util/event.util';
import {Event} from '../../model/event';
import {ApiService} from './../../services/api.service';
import {Router} from '@angular/router';

export enum CreateEventFormFields {
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
  selector: 'app-event-create',
  templateUrl: './event-create.component.html',
  styleUrls: ['./event-create.component.scss'],
})
export class EventCreateComponent implements OnInit {
  CreateEventFormFields = CreateEventFormFields;
  EventCategory = EventCategory;

  roles: string[] = [];

  createEventForm!: FormGroup;
  pictureFile: File | undefined;
  fileList: NzUploadFile[] = [];

  loadingMessage: NzMessageRef | null = null;

  isProcessing = false;

  event$ = new Subject<Event>();

  destroy$ = new Subject<void>();

  constructor(
    private ApiService: ApiService,
    private formBuilder: FormBuilder,
    private message: NzMessageService,
    private AuthService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.createEventForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      location: ['', [Validators.required]],
      society: ['', [Validators.required]],
      maxParticipation: ['', [Validators.required]],
      applyDeadline: ['', [Validators.required]],
      date: ['', [Validators.required]],
      category: ['', [Validators.required]],
      description: ['', [Validators.required]],
      fee: ['', [Validators.required]],
    });

    this.AuthService.user$.pipe(filter(user => !!user)).subscribe(user => (this.roles = [...user!.roles]));

    this.event$
      .pipe(
        switchMap(event => this.ApiService.createEvent(event, this.pictureFile!, this.createEventForm.value.society)),
        tap(() => this.message.remove(this.loadingMessage?.messageId)),
        tap(() => this.message.success('Successfully created event', {nzDuration: 2000})),
        tap(() => this.router.navigate([Path.Main, Path.Event]))
      )
      .subscribe({
        error: err => {
          this.message.error('Unable to create events', {nzDuration: 2000});
        },
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

    this.isProcessing = true;
    this.loadingMessage = this.message.loading('Creating event...');
    this.event$.next(convertFormDataToEvent('', {...this.createEventForm.value}));
  }

  saveFileBuffer({file}: NzUploadChangeParam): void {
    if (this.fileList.length > 1) {
      this.fileList = this.fileList.slice(-1);
    }
    this.fileList[0] = {...this.fileList[0], status: 'done'};
    this.pictureFile = file.originFileObj;
  }

  backToEventPage(): void {
    this.router.navigate([Path.Main, Path.Event]);
  }
}
