package ngok3.fyp.backend.operation.student

import io.swagger.v3.oas.annotations.Operation
import ngok3.fyp.backend.operation.PaginationCountDto
import ngok3.fyp.backend.operation.event.dto.EventDto
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/student")
class StudentController(
    private val studentService: StudentService
) {
    @Operation(summary = "Get student profile from database by itsc")
    @GetMapping
    fun getStudentProfile(
        @RequestParam(name = "itsc", required = false, defaultValue = "") itsc: String,
        @RequestParam(name = "uuid", required = false, defaultValue = "") uuid: String,
        @RequestParam(name = "cardId", required = false, defaultValue = "") cardId: String,
    ): StudentDto {
        return studentService.getStudentProfile(itsc, uuid, cardId)
    }

    @Operation(summary = "Count all event by student id with society member")
    @GetMapping("/{studentId}/event/totalNumber")
    fun countAllEventWithSocietyMember(
        @PathVariable studentId: String,
    ): PaginationCountDto {
        return studentService.countAllEventWithSocietyMember(studentId)
    }

    @Operation(summary = "Get all event by student with society member")
    @GetMapping("/{studentId}/event")
    fun getAllEventWithSocietyMember(
        @PathVariable studentId: String,
        @RequestParam("pageNum", required = false, defaultValue = "0") pageNum: Int,
        @RequestParam("pageSize", required = false, defaultValue = "10") pageSize: Int
    ): List<EventDto> {
        return studentService.getAllEventWithSocietyMember(studentId, pageNum, pageSize)
    }

    @Operation(summary = "Get all event by student with society member")
    @GetMapping("/societyStatus")
    fun getStudentSocietyStatus(
        @RequestParam("itsc") itsc: String
    ): List<StudentEnrolledSocietyStatusDto> {
        return studentService.getStudentSocietyStatus(itsc)
    }
}

