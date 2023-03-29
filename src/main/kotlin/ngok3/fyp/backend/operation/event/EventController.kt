package ngok3.fyp.backend.operation.event

import io.swagger.v3.oas.annotations.Operation
import ngok3.fyp.backend.operation.event.dto.EventDto
import ngok3.fyp.backend.operation.event.dto.JoinEventDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/event")
class EventController(
    private val eventService: EventService
) {
    @Operation(summary = "get all event from all society with pagination")
    @GetMapping
    fun getAllSocietyEvent(
        @RequestParam("pageNum", required = false, defaultValue = "0") pageNum: Int,
        @RequestParam("pageSize", required = false, defaultValue = "10") pageSize: Int
    ): List<EventDto> {
        return eventService.getAllEvent(pageNum, pageSize)
    }

    @Operation(summary = "join event with student itsc and event id")
    @PostMapping("/join")
    fun joinEvent(@RequestBody joinEventDto: JoinEventDto): Boolean {
        return eventService.joinEvent(joinEventDto.itsc, joinEventDto.eventId)
    }

    @Operation(summary = "create event with detail")
    @PostMapping
    fun createEvent(
        @CookieValue("token") jwtToken: String,
        @RequestPart("poster") uploadFile: MultipartFile,
        @RequestPart("event") eventDto: EventDto,
        @RequestPart("society") societyName: String,
    ): EventDto {
        return eventService.createEvent(jwtToken, uploadFile, eventDto, societyName)
    }

    @Operation(summary = "delete event with event id")
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{eventId}")
    fun deleteEvent(
        @CookieValue("token") jwtToken: String,
        @PathVariable eventId: String,
    ) {
        eventService.deleteEvent(jwtToken, eventId)
    }

    @Operation(summary = "update event with event id")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/{eventId}")
    fun updateEvent(
        @CookieValue("token") jwtToken: String,
        @RequestPart("poster") uploadFile: MultipartFile,
        @RequestPart("event") eventDto: EventDto,
        @PathVariable eventId: String,
    ) {
        eventService.updateEvent(jwtToken, eventId, eventDto, uploadFile)
    }

    @Operation(summary = "get event with event id")
    @GetMapping("/{eventId}")
    fun getEventWithEventId(
        @CookieValue("token") jwtToken: String,
        @PathVariable eventId: String,
    ): EventDto {
        return eventService.getEventWithUuid(jwtToken, eventId)
    }

    @Operation(summary = "get event with society name")
    @GetMapping("/society/{societyName}")
    fun getEventWithSocietyName(
        @CookieValue("token") jwtToken: String,
        @PathVariable societyName: String,
        @RequestParam("pageNum", required = false, defaultValue = "0") pageNum: Int,
        @RequestParam("pageSize", required = false, defaultValue = "10") pageSize: Int
    ): List<EventDto> {
        return eventService.getEventWithSocietyName(jwtToken, societyName, pageNum, pageSize)
    }
}