package ngok3.fyp.backend.event

import ngok3.fyp.backend.student.StudentEntity
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface EventRepository : PagingAndSortingRepository<EventEntity, Long> {

    fun findByName(name: String): Optional<EventEntity>


    fun findByApplyDeadlineGreaterThanEqualOrderByApplyDeadlineAsc(
        applyDeadline: LocalDateTime,
        pageable: Pageable
    ): Page<EventEntity>


    fun findBySocietyEntity_EnrolledSocietyRecordEntities_StudentEntityOrderByApplyDeadlineAsc(
        studentEntity: StudentEntity,
        pageable: Pageable
    ): Page<EventEntity>

}