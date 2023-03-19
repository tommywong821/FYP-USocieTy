package ngok3.fyp.backend.operation.student

import org.springframework.stereotype.Service
import java.util.*

@Service
class StudentService(
    private val studentRepository: StudentRepository,
) {

    fun getStudentProfile(itsc: String, uuid: String, cardId: String): StudentDto {
        val studentEntity: StudentEntity = when {
            itsc.isNotBlank() -> studentRepository.findByItsc(itsc)
            uuid.isNotBlank() -> studentRepository.findById(UUID.fromString(uuid))
            else -> studentRepository.findByCardId(cardId)
        }.orElseThrow {
            Exception("student is not found in database")
        }
        val enrolledSocietyList: List<String> =
            studentEntity.enrolledSocietyRecordEntities.map { it.societyEntity.name }

        return StudentDto(studentEntity, enrolledSocietyList)
    }
}