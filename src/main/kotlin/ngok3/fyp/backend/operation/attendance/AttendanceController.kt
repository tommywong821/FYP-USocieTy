package ngok3.fyp.backend.operation.attendance

import com.google.gson.Gson
import io.swagger.v3.oas.annotations.Operation
import ngok3.fyp.backend.operation.attendance.model.AttendanceDto
import ngok3.fyp.backend.operation.attendance.model.StudentAttendanceDto
import ngok3.fyp.backend.util.RSAUtil
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/attendance")
class AttendanceController(
    private val rsaUtil: RSAUtil,
    private val attendanceService: AttendanceService
) {
    @Operation(summary = "create attendance of student in event ")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    fun createAttendance(@RequestBody encryptedAttendanceData: String) {
        val decryptedData = rsaUtil.decryptMessage(
            encryptedAttendanceData.replace("\"", "").replace("\\", "")
        )
        val attendanceDto: AttendanceDto = Gson().fromJson(decryptedData, AttendanceDto::class.java)

        attendanceService.createAttendance(
            studentId = attendanceDto.studentId,
            eventId = attendanceDto.eventId,
            userItsc = attendanceDto.userItsc,
            currentTime = attendanceDto.currentTime
        )
    }

    @Operation(summary = "get all attendance")
    @GetMapping
    fun getAllAttendance(): List<StudentAttendanceDto> {
        return attendanceService.getAllAttendance()
    }

    @Operation(summary = "delete attendance with student id and event Id")
    @DeleteMapping
    fun deleteAttendance(
        @RequestParam("studentUuid") encryptedStudentUuid: String,
        @RequestParam("eventUuid") encryptedEventUuid: String
    ): ResponseEntity<Void> {
        val studentUuid: String = rsaUtil.decryptMessage(encryptedStudentUuid)
        val eventUuid: String = rsaUtil.decryptMessage(encryptedEventUuid)
        return if (attendanceService.deleteAttendance(studentUuid, eventUuid)) {
            ResponseEntity.accepted().build()
        } else {
            ResponseEntity.notFound().build()
        }
    }
}