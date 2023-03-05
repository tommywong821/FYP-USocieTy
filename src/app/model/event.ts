export interface Event {
  id?: string;
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

export enum EventProperty {
  id,
  name,
  poster,
  maxParticipation,
  applyDeadline,
  location,
  startDate,
  endDate,
  category,
  description,
  fee,
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
  OrientationCamp = 'orientation camp',
  Workshop = 'workshop',
  VoluntaryWork = 'voluntary work',
  Other = 'other',
}
