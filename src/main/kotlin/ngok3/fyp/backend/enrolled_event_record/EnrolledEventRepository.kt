package ngok3.fyp.backend.enrolled_event_record

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime

interface EnrolledEventRepository : CrudRepository<EnrolledEventRecordEntity, EnrolledEventRecordKey> {

    @Query(
        """select e from EnrolledEventRecordEntity e
where e.studentEntity.itsc = ?1 and e.eventEntity.startDate >= ?2
order by e.eventEntity.startDate"""
    )
    fun findByStudentEntity_ItscAndEventEntity_StartDateGreaterThanEqualOrderByEventEntity_StartDateAsc(
        itsc: String,
        startDate: LocalDateTime
    ): List<EnrolledEventRecordEntity>

}