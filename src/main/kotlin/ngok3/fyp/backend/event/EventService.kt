package ngok3.fyp.backend.event

import ngok3.fyp.backend.student.StudentEntity
import ngok3.fyp.backend.student.StudentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*


@Service
class EventService(
    @Autowired val eventRepository: EventRepository,
    @Autowired val studentRepository: StudentRepository
) {
    fun getAllSocietyEvent(itsc: String, pageNum: Int, pageSize: Int): List<EventDto> {
        val firstPageNumWithPageSizeElement: Pageable = PageRequest.of(pageNum, pageSize)

        val allEvent: List<EventEntity> = if (itsc.isBlank()) {
            //get all event
            eventRepository.findByApplyDeadlineGreaterThanEqualOrderByApplyDeadlineAsc(
                LocalDateTime.now(), firstPageNumWithPageSizeElement
            ).content

        } else {
            //get all event from sid
            val studentEntity: Optional<StudentEntity> = studentRepository.findByItsc(itsc)
            if (studentEntity.isEmpty) {
                throw Exception("Student with $itsc not found")
            }
            eventRepository.findBySocietyEntity_EnrolledSocietyRecordEntities_StudentEntityOrderByApplyDeadlineAsc(
                studentEntity.get(),
                firstPageNumWithPageSizeElement
            ).content
        }

        return allEvent.map { event ->
            EventDto(
                event.name,
                event.poster,
                event.maxParticipation,
                event.applyDeadline,
                event.location
            )
        }
    }
}