import {Observable, ReplaySubject} from 'rxjs';
import {
  createEventEndpoint,
  CreateEventRequest,
  CreateEventRequestBody,
  EventDto,
  updateEventEndpoint,
  UpdateEventRequest,
  UpdateEventRequestBody,
} from 'src/app/api/event';
import {Event, EventFormData} from 'src/app/model/event';
import {User} from 'src/app/model/user';

export function convertFormDataToEvent(eventId: string, formData: EventFormData): Event {
  const startDate = formData.date[0];
  const endDate = formData.date[1];
  startDate.setHours(startDate.getHours() - 8);
  endDate.setHours(endDate.getHours() - 8);
  const event: Event = {
    id: eventId,
    name: formData.name,
    poster: formData.poster,
    maxParticipation: formData.maxParticipation,
    applyDeadline: formData.applyDeadline,
    location: formData.location,
    startDate,
    endDate,
    category: formData.category,
    description: formData.description,
    fee: formData.fee,
  };
  return event;
}

export function convertFiletoBase64(file: File): Observable<string> {
  const result = new ReplaySubject<string>(1);
  const reader = new FileReader();
  reader.readAsBinaryString(file);
  reader.onload = event => result.next(btoa(event.target!.result!.toString()));
  return result;
}

export function convertEventToEventDto(event: Event): EventDto {
  return {
    id: '',
    name: event.name,
    poster: event.poster,
    maxParticipation: event.maxParticipation,
    applyDeadline: event.applyDeadline.toLocaleDateString(),
    location: event.location,
    startDate: event.startDate.toLocaleDateString(),
    endDate: event.endDate.toLocaleDateString(),
    category: event.category,
    description: event.description,
    fee: event.fee,
  };
}

export function getCreateEventRequest(event: Event, society: string, user: User): CreateEventRequest {
  const body: CreateEventRequestBody = {
    eventDto: convertEventToEventDto(event),
    itsc: user.itsc,
    society,
  };

  const request: CreateEventRequest = {
    endpoint: createEventEndpoint,
    body,
  };

  return request;
}

export function getUpdateEventRequest(eventId: string, event: Event, society: string, user: User): UpdateEventRequest {
  const body: UpdateEventRequestBody = {
    eventDto: convertEventToEventDto(event),
    itsc: user.itsc,
    society,
  };

  const urlParams = {id: eventId};

  const request: UpdateEventRequest = {
    endpoint: updateEventEndpoint,
    urlParams,
    body,
  };

  return request;
}

export function getPictureNameFromUrl(url: string): string {
  const index = url.lastIndexOf('/');

  return url.substr(index).replace('/', '');
}

export function convertStringToDate(date: Date): Date {
  const str = date as unknown as string;
  const [day, month, year, hour, minute] = str.split(/[/\s:]/g);
  return new Date(`${year}-${month}-${day}T${hour}:${minute}:00`);
}
