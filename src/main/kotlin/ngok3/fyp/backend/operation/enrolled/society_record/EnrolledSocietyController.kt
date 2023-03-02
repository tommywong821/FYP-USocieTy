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


    @Operation(summary = "get all studnet records with enrolled society status not equal SUCCESS")
    @GetMapping
    fun getEnrolledSocietyRecord(
        @CookieValue("token") jwtToken: String,
        @RequestParam("societyName") societyName: String,
    ): List<StudentEnrolledEventRecord> {
        return enrolledSocietyService.getEnrolledSocietyRecord(jwtToken, societyName)
    }
}