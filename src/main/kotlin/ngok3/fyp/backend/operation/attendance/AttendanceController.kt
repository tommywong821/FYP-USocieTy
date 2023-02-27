package ngok3.fyp.backend.operation.attendance

import com.google.gson.Gson
import io.swagger.v3.oas.annotations.Operation
import ngok3.fyp.backend.util.RSAUtil
import org.springframework.http.HttpStatus
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
    fun createAttendance(@RequestBody encryptedAttendanceData: EncryptedAttendanceData) {
        val decryptedData = rsaUtil.decryptMessage(encryptedAttendanceData.data)
        val attendanceDto: AttendanceDto = Gson().fromJson(decryptedData, AttendanceDto::class.java)
        attendanceService.createAttendance(studentId = attendanceDto.studentId, eventId = attendanceDto.eventId)
    }
}