package ngok3.fyp.backend.operation.enrolled_event_record

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/enrolledEventRecord")
class EnrolledEventRecordController(
    private val enrolledEventService: EnrolledEventService
) {
    @Operation(summary = "update enrolled event record")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping
    fun updateEnrolledEventRecord(
        @CookieValue("token") jwtToken: String,
        @RequestBody updateEnrolledEventRecordDto: UpdateEnrolledEventRecordDto
    ) {
        return enrolledEventService.updateEnrolledEventService(jwtToken, updateEnrolledEventRecordDto)
    }
}