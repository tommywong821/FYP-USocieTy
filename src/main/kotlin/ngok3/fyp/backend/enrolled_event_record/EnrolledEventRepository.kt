package ngok3.fyp.backend.enrolled_event_record

import org.springframework.data.repository.CrudRepository

interface EnrolledEventRepository : CrudRepository<EnrolledEventRecordEntity, EnrolledEventRecordKey> {
}