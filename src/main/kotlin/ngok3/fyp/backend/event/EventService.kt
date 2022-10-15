package ngok3.fyp.backend.event

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service


@Service
class EventService(
    @Autowired val eventRepository: EventRepository
) {
    fun getAllSocietyEvent(sid: String, pageNum: Int, pageSize: Int): List<EventDto> {
        if (sid.isBlank()) {
            //get all event
            val firstPageNumWithPageSizeElement: Pageable = PageRequest.of(pageNum, pageSize)
            val allEvent: List<EventEntity> = eventRepository.findAll(firstPageNumWithPageSizeElement).content
            return allEvent.map { event ->
                EventDto(
                    event.name,
                    event.poster,
                    event.maxParticipation,
                    event.applyDeadline
                )
            }
        }
        //get all event from sid
        return emptyList()
    }
}