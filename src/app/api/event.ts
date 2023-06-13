import {HttpParams} from '@angular/common/http';
import {Request} from './common';

export const createEventEndpoint = 'event';
export const updateEventEndpoint = 'event';

export interface EventDto {
  id: string;
  name: string;
  poster: string;
  maxParticipation: number;
  applyDeadline: string;
  location: string;
  startDate: string;
  endDate: string;
  category: string;
  description: string;
  fee: number;
}

export interface CreateEventRequestBody {
  eventDto: EventDto;
  itsc: string;
  society: string;
}

export interface CreateEventRequest extends Request {
  body: CreateEventRequestBody;
}

export interface GetEventRequest extends Request {
  urlParams: Record<string, string>;
}

export interface GetEventsRequest extends Request {
  queryParam: HttpParams;
}

export interface UpdateEventRequestBody {
  eventDto: EventDto;
  itsc: string;
  society: string;
}

export interface UpdateEventRequest extends Request {
  body: UpdateEventRequestBody;
  urlParams: Record<string, string>;
}

export interface DeleteEventRequest extends Request {
  urlParams: Record<string, string>;
}
