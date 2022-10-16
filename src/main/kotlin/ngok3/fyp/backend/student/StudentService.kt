package ngok3.fyp.backend.student

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class StudentService(
    @Autowired val studentRepository: StudentRepository,
) {

    fun getStudentProfile(itsc: String): StudentDto {
        val studentEntityOpt: Optional<StudentEntity> = studentRepository.findByItsc(itsc)
        if (studentEntityOpt.isEmpty) {
            throw Exception("Student with $itsc is not found")
        }
        val studentEntity = studentEntityOpt.get()
        return StudentDto(studentEntity.itsc, studentEntity.name, studentEntity.mail, studentEntity.role)
    }
}