import {Request} from './common';

export const createEventEndpoint = 'event';

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
