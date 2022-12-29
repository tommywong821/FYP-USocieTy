package ngok3.fyp.backend.event

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
        @RequestParam("itsc", required = false, defaultValue = "") itsc: String,
        @RequestParam("pageNum", required = false, defaultValue = "0") pageNum: Int,
        @RequestParam("pageSize", required = false, defaultValue = "10") pageSize: Int
    ): List<EventDto> {
        print("itsc: $itsc pageSize: $pageSize pageNum: $pageNum ")
        return eventService.getAllSocietyEvent(itsc, pageNum, pageSize)
    }

    @PostMapping
    fun joinSocietyEvent(
        @RequestParam("itsc", required = false, defaultValue = "") itsc: String,
        @RequestParam("eventName", required = false, defaultValue = "") eventName: String,
    ) {
        eventService.joinSocietyEvent(itsc, eventName)
    }
}