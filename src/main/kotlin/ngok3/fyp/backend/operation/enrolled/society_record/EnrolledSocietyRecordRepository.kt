package ngok3.fyp.backend.operation.enrolled.society_record

import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
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


    @Query("select e from EnrolledSocietyRecordEntity e where e.societyEntity.name = ?1 and e.status != ?2")
    fun findBySocietyEntity_NameAndStatusNotEqual(
        name: String,
        status: EnrolledStatus
    ): List<EnrolledSocietyRecordEntity>

}