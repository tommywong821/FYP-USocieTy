package ngok3.fyp.backend.operation.enrolled.event_record

import io.swagger.v3.oas.annotations.Operation
import ngok3.fyp.backend.operation.PaginationCountDto
import ngok3.fyp.backend.operation.enrolled.event_record.model.EnrolledEventDto
import ngok3.fyp.backend.operation.enrolled.event_record.model.StudentEnrolledEventRecordDto
import ngok3.fyp.backend.operation.enrolled.event_record.model.UpdateEnrolledEventRecordDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/enrolledEventRecord")
class EnrolledEventRecordController(
    private val enrolledEventService: EnrolledEventRecordService
) {
    @Operation(summary = "update enrolled event record")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    fun updateEnrolledEventRecord(
        @CookieValue("token") jwtToken: String,
        @RequestBody updateEnrolledEventRecordDtoList: List<UpdateEnrolledEventRecordDto>
    ) {
        return enrolledEventService.updateEnrolledEventRecord(jwtToken, updateEnrolledEventRecordDtoList)
    }

    @Operation(summary = "get all enrolled event of student with itsc")
    @GetMapping
    fun getAllEnrolledEvent(
        @RequestParam("itsc", required = false, defaultValue = "") itsc: String,
        @RequestParam("pageNum", required = false, defaultValue = "0") pageNum: Int,
        @RequestParam("pageSize", required = false, defaultValue = "10") pageSize: Int
    ): List<EnrolledEventDto> {
        print("itsc: $itsc pageSize: $pageSize pageNum: $pageNum ")
        return enrolledEventService.getAllEnrolledEvent(itsc, pageNum, pageSize)
    }


    @Operation(summary = "count all enrolled event of student with itsc")
    @GetMapping("/count")
    fun countAllEnrolledEvent(
        @RequestParam("itsc", required = false, defaultValue = "") itsc: String
    ): Long {
        return enrolledEventService.countEnrolledEvent(itsc)
    }

    @Operation(summary = "get all students of an enrolled event with event id")
    @GetMapping("/{eventId}")
    fun getStudentEnrolledEventRecord(
        @CookieValue("token") jwtToken: String,
        @PathVariable eventId: String,
        @RequestParam("pageNum", required = false, defaultValue = "0") pageNum: Int,
        @RequestParam("pageSize", required = false, defaultValue = "10") pageSize: Int
    ): List<StudentEnrolledEventRecordDto> {
        return enrolledEventService.getStudentEnrolledEventRecord(jwtToken, eventId, pageNum, pageSize)
    }

    @Operation(summary = "count all enrolled event with event id")
    @GetMapping("/{eventId}/totalNumber")
    fun getStudentEnrolledEventRecord(
        @PathVariable eventId: String,
    ): PaginationCountDto {
        return enrolledEventService.countStudentEnrolledEventRecord(eventId)
    }
}