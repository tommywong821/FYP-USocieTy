package ngok3.fyp.backend.operation.student

import org.springframework.stereotype.Service
import java.util.*

@Service
class StudentService(
    private val studentRepository: StudentRepository,
) {

    fun getStudentProfile(itsc: String, uuid: String, cardId: String): StudentDto {
        val studentEntityOpt: Optional<StudentEntity> = when {
            itsc.isNotBlank() -> studentRepository.findByItsc(itsc)
            uuid.isNotBlank() -> studentRepository.findById(UUID.fromString(uuid))
            else -> studentRepository.findByCardId(cardId)
        }

        val studentEntity = studentEntityOpt.orElseGet {
            studentRepository.save(StudentEntity(itsc, "", "${itsc}@connect.ust.hk"))
        }
        val enrolledSocietyList: List<String> =
            studentEntity.enrolledSocietyRecordEntities.map { it.societyEntity.name }

        return StudentDto(studentEntity, enrolledSocietyList)
    }
}