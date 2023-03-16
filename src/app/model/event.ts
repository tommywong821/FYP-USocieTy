export interface Event {
  name: string;
  poster: string;
  maxParticipation: number;
  applyDeadline: Date;
  location: string;
  startDate: Date;
  endDate: Date;
  category: EventCategory;
  description: string;
  fee: number;
}

export interface EventFormData {
  name: string;
  poster: string;
  maxParticipation: number;
  applyDeadline: Date;
  location: string;
  date: Date[];
  category: EventCategory;
  description: string;
  fee: number;
}

export enum EventCategory {
  Sport = 'Sport',
  Outdoor = 'Outdoor',
  Indoor = 'Indoor',
}
