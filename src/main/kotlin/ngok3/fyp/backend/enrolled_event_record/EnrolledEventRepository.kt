package ngok3.fyp.backend.enrolled_event_record

import ngok3.fyp.backend.enrolled_society_record.EnrolledSocietyRecordKey
import org.springframework.data.repository.CrudRepository

interface EnrolledEventRepository : CrudRepository<EnrolledEventRecordEntity, EnrolledSocietyRecordKey> {
}