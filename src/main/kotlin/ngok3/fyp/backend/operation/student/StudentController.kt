package ngok3.fyp.backend.operation.student

import io.swagger.v3.oas.annotations.Operation
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

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
    ): StudentDto {
        return studentService.getStudentProfile(itsc, uuid)
    }
}

