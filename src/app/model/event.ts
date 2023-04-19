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
  society?: string;
  version?: number;
}

export interface EventDto {
  id?: string;
  name: string;
  poster: string;
  maxParticipation: number;
  applyDeadline: string;
  location: string;
  startDate: string;
  endDate: string;
  category: EventCategory;
  description: string;
  fee: number;
  society?: string;
  version?: number;
}

export enum EventProperty {
  Id = 'Id',
  Name = 'Name',
  Poster = 'Poster',
  MaxParticipation = 'Max Participation',
  ApplyDeadline = 'Application Deadline',
  Location = 'Location',
  StartDate = 'Start Date',
  EndDate = 'End Date',
  Category = 'Category',
  Description = 'Description',
  Fee = 'Fee',
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
  Sport = 'SPORT',
  Outdoor = 'OUTDOOR',
  Indoor = 'INDOOR',
}

export enum PaymentStatus {
  Paid = 'PAID',
  Unpaid = 'UNPAID',
}

export enum EventEnrollmentStatus {
  Pending = 'PENDING',
  Enrolled = 'SUCCESS',
  Rejected = 'DECLINE',
}

export interface EventEnrollmentRecord {
  itsc: string;
  studentId: string;
  paymentStatus: PaymentStatus;
  enrolledStatus: EventEnrollmentStatus;
}

export interface UpdateEventEnrollmentRecordPayload {
  eventId: string;
  studentId: string;
  paymentStatus: PaymentStatus;
  enrolledStatus: EventEnrollmentStatus;
}

export enum EventAction {
  Create = 'CREATE',
  Update = 'UPDATE',
  Fetch = 'FETCH',
  Delete = 'DELETE',
}

export interface EventAttendance {
  studentItsc: string;
  studentName: string;
  attendanceCreatedAt: Date;
}
