package ngok3.fyp.backend.operation.event

import io.swagger.v3.oas.annotations.Operation
import ngok3.fyp.backend.operation.enrolled_event_record.EnrolledEventDto
import ngok3.fyp.backend.operation.event.dto.EventDto
import ngok3.fyp.backend.operation.event.dto.JoinEventDto
import org.springframework.security.access.prepost.PreAuthorize
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
        print("pageSize: $pageSize pageNum: $pageNum ")
        return eventService.getAllEvent(pageNum, pageSize)
    }

    @Operation(summary = "join event with student itsc and event id")
    @PostMapping("/join")
    fun joinEvent(@RequestBody joinEventDto: JoinEventDto): Boolean {
        return eventService.joinEvent(joinEventDto.itsc, joinEventDto.eventId)
    }

    @Operation(summary = "get all enrolled event of student with itsc")
    @GetMapping("/enrolled")
    fun getAllEnrolledEvent(
        @RequestParam("itsc", required = false, defaultValue = "") itsc: String,
        @RequestParam("pageNum", required = false, defaultValue = "0") pageNum: Int,
        @RequestParam("pageSize", required = false, defaultValue = "10") pageSize: Int
    ): List<EnrolledEventDto> {
        print("itsc: $itsc pageSize: $pageSize pageNum: $pageNum ")
        return eventService.getAllEnrolledEvent(itsc, pageNum, pageSize)
    }

    @Operation(summary = "count all enrolled event of student with itsc")
    @GetMapping("/enrolled/count")
    fun countAllEnrolledEvent(
        @RequestParam("itsc", required = false, defaultValue = "") itsc: String
    ): Long {
        return eventService.countEnrolledEvent(itsc)
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_SOCIETY_MEMBER')")
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
}