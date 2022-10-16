package ngok3.fyp.backend.student

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/student")
class StudentController(
    @Autowired val studentService: StudentService
) {
    @GetMapping()
    fun getStudentProfile(
        @RequestParam("itsc") itsc: String
    ): StudentDto {
        return studentService.getStudentProfile(itsc)
    }
}

