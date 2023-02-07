package ngok3.fyp.backend.operation.enrolled_society_record

import ngok3.fyp.backend.operation.enrolled_event_record.EnrolledStatus
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface EnrolledSocietyRecordRepository : CrudRepository<EnrolledSocietyRecordEntity, EnrolledSocietyRecordKey> {
    @Query(
        """select e from EnrolledSocietyRecordEntity e
where e.studentEntity.itsc = ?1 and e.societyEntity.name = ?2 and e.status = ?3"""
    )
    fun findByItscAndSocietyNameAndEnrolledStatus(
        itsc: String,
        societyName: String,
        status: EnrolledStatus
    ): Optional<EnrolledSocietyRecordEntity>

}