package ngok3.fyp.backend.event

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/event")
class EventController(
    @Autowired val eventService: EventService
) {
    @GetMapping
    fun getAllSocietyEvent(
        @RequestParam("sid", required = false, defaultValue = "") sid: String,
        @RequestParam("pageNum", required = false, defaultValue = "0") pageNum: Int,
        @RequestParam("pageSize", required = false, defaultValue = "10") pageSize: Int
    ): List<EventDto> {
        print("sid: $sid pageSize: $pageSize pageNum: $pageNum ")
        return eventService.getAllSocietyEvent(sid, pageNum, pageSize)
    }
}