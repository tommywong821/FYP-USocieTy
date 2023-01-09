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
            //create record when 1st login
            return StudentDto(studentRepository.save(StudentEntity(itsc, "", "itsc@connect.ust.hk", "")))
        }
        val studentEntity = studentEntityOpt.get()
        return StudentDto(studentEntity.itsc, studentEntity.nickname, studentEntity.mail, studentEntity.role)
    }
}