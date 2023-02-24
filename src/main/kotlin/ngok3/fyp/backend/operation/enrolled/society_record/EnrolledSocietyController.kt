package ngok3.fyp.backend.operation.enrolled.society_record

import io.swagger.v3.oas.annotations.Operation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/enrolledSocietyRecord")
class EnrolledSocietyController(
    private val enrolledSocietyService: EnrolledSocietyRecordService
) {

    @Operation(summary = "update enrolled society record")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping
    fun updateEnrolledSocietyRecord(
        @CookieValue("token") jwtToken: String,
        @RequestBody updateEnrolledSocietyRecordDto: UpdateEnrolledSocietyRecordDto
    ) {
        return enrolledSocietyService.updateEnrolledSocietyRecord(jwtToken, updateEnrolledSocietyRecordDto)
    }
}