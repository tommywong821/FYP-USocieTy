import {Observable, ReplaySubject} from 'rxjs';
import {createEventEndpoint, CreateEventRequest, CreateEventRequestBody, EventDto} from 'src/app/api/event';
import {Event, EventFormData} from 'src/app/model/event';
import {User} from 'src/app/model/user';

export function convertFormDataToEvent(formData: EventFormData): Event {
  const event: Event = {
    name: formData.name,
    poster: formData.poster,
    maxParticipation: formData.maxParticipation,
    applyDeadline: formData.applyDeadline,
    location: formData.location,
    startDate: formData.date[0],
    endDate: formData.date[1],
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
    queryParam: null,
    body,
  };

  return request;
}
