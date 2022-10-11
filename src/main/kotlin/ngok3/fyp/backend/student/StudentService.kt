package ngok3.fyp.backend.student

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class StudentService(
    @Autowired val studentRepository: StudentRepository,
) {

    fun getStudentProfile(itsc: String) {

    }
}