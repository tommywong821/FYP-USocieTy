package ngok3.fyp.backend.operation.enrolled_event_record

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.time.LocalDateTime
import java.util.*

interface EnrolledEventRecordRepository : CrudRepository<EnrolledEventRecordEntity, EnrolledEventRecordKey> {

    @Query(
        """select e from EnrolledEventRecordEntity e
where e.studentEntity.itsc = ?1 and e.eventEntity.startDate >= ?2
order by e.eventEntity.startDate"""
    )
    fun findByStudentEntity_ItscAndEventEntity_StartDateGreaterThanEqualOrderByEventEntity_StartDateAsc(
        itsc: String,
        startDate: LocalDateTime
    ): List<EnrolledEventRecordEntity>


    @Query("select count(e) from EnrolledEventRecordEntity e where e.id.eventUuid = ?1")
    fun countById_EventUuid(eventUuid: UUID): Long


    @Query(
        """select count(e) from EnrolledEventRecordEntity e
where e.studentEntity.itsc = ?1 and e.eventEntity.startDate >= ?2"""
    )
    fun countByStudentEntity_ItscAndEventEntity_StartDateGreaterThanEqual(itsc: String, startDate: LocalDateTime): Long

}