package ngok3.fyp.backend.operation.student

import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Service
import java.util.*

@Service
class StudentService(
    private val studentRepository: StudentRepository,
) {

    fun getStudentProfile(itsc: String, uuid: String): StudentDto {
        val studentEntityOpt: Optional<StudentEntity> =
            if (StringUtils.isBlank(itsc)) studentRepository.findById(UUID.fromString(uuid)) else studentRepository.findByItsc(
                itsc
            )

        val studentEntity = studentEntityOpt.orElseGet {
            studentRepository.save(StudentEntity(itsc, "", "${itsc}@connect.ust.hk"))
        }
        val enrolledSocietyList: List<String> =
            studentEntity.enrolledSocietyRecordEntities.map { it.societyEntity.name }

        return StudentDto(studentEntity, enrolledSocietyList)
    }
}