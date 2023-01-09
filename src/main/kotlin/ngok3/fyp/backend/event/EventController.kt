package ngok3.fyp.backend.event

import ngok3.fyp.backend.enrolled_event_record.EnrolledEventDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/event")
class EventController(
    @Autowired val eventService: EventService
) {
    @GetMapping
    fun getAllSocietyEvent(
        @RequestParam("pageNum", required = false, defaultValue = "0") pageNum: Int,
        @RequestParam("pageSize", required = false, defaultValue = "10") pageSize: Int
    ): List<EventDto> {
        print("pageSize: $pageSize pageNum: $pageNum ")
        return eventService.getAllSocietyEvent(pageNum, pageSize)
    }

    @PostMapping
    fun joinEvent(
        @RequestParam("itsc", required = false, defaultValue = "") itsc: String,
        @RequestParam("eventId", required = false, defaultValue = "") eventId: String,
    ) {
        eventService.joinEvent(itsc, eventId)
    }

    @GetMapping
    @RequestMapping("/enrolled")
    fun getAllEnrolledEvent(
        @RequestParam("itsc", required = false, defaultValue = "") itsc: String,
        @RequestParam("pageNum", required = false, defaultValue = "0") pageNum: Int,
        @RequestParam("pageSize", required = false, defaultValue = "10") pageSize: Int
    ): List<EnrolledEventDto> {
        print("itsc: $itsc pageSize: $pageSize pageNum: $pageNum ")
        return eventService.getAllEnrolledEvent(itsc, pageNum, pageSize)
    }
}