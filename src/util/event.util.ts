import {Event, EventFormData} from 'src/app/model/event';

export function convertFormDataToEvent(formData: EventFormData): Event {
  const event: Event = {
    name: formData.name,
    poster: formData.poster,
    maxParticipations: formData.maxParticipations,
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
