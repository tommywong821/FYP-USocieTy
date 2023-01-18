export interface Event {
  name: string;
  poster: string;
  maxParticipations: number;
  applyDeadline: Date;
  location: string;
  startDate: Date;
  endDate: Date;
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
