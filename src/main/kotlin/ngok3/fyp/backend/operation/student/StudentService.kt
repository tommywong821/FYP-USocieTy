package ngok3.fyp.backend.operation.student

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class StudentService(
    @Autowired val studentRepository: StudentRepository,
) {

    fun getStudentProfile(itsc: String): StudentDto {
        val studentEntityOpt: Optional<StudentEntity> = studentRepository.findByItsc(itsc)
        val studentEntity = studentEntityOpt.orElseGet {
            studentRepository.save(StudentEntity(itsc, "", "${itsc}@connect.ust.hk"))
        }
        val enrolledSocietyList: StringBuilder = StringBuilder()
        var prefix = ""
        for (enrolledSociety in studentEntity.enrolledSocietyRecordEntity) {
            enrolledSocietyList.append(prefix)
            prefix = ","
            enrolledSocietyList.append(enrolledSociety.societyEntity?.name)
        }
        return StudentDto(studentEntity, enrolledSocietyList.toString())
    }
}