package ngok3.fyp.backend.operation.student

import ngok3.fyp.backend.authentication.student_role.StudentRoleEntity
import ngok3.fyp.backend.operation.TotalCountDto
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordEntity
import ngok3.fyp.backend.operation.event.EventEntity
import ngok3.fyp.backend.operation.event.EventEntityRepository
import ngok3.fyp.backend.operation.event.dto.EventDto
import ngok3.fyp.backend.operation.student.model.StudentDto
import ngok3.fyp.backend.operation.student.model.StudentEnrolledSocietyStatusDto
import ngok3.fyp.backend.util.DateUtil
import ngok3.fyp.backend.util.JWTUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import java.util.*

@Service
class StudentService(
    private val studentRepository: StudentEntityRepository,
    private val jwtUtil: JWTUtil, private val eventRepository: EventEntityRepository,
    private val dateUtil: DateUtil
) {
    @Value("\${aws.bucket.domain}")
    val s3BucketDomain: String = ""
    fun getStudentProfile(itsc: String, uuid: String, cardId: String): StudentDto {
        val studentEntity: StudentEntity = when {
            itsc.isNotBlank() -> studentRepository.findByItsc(itsc)
            uuid.isNotBlank() -> studentRepository.findById(UUID.fromString(uuid))
            else -> studentRepository.findByCardId(cardId)
        }.orElseThrow {
            Exception("student is not found in database")
        }


        val enrolledSocietyList: List<String> =
            studentEntity.enrolledSocietyRecordEntities.map { enrolledSocietyRecordEntity: EnrolledSocietyRecordEntity -> enrolledSocietyRecordEntity.societyEntity.name }
                .sorted()

        return StudentDto(studentEntity, enrolledSocietyList)
    }

    fun getAllEventWithSocietyMember(jwtToken: String, studentId: String, pageNum: Int, pageSize: Int): List<EventDto> {
        val student: StudentEntity = studentRepository.findById(UUID.fromString(studentId)).orElseThrow {
            Exception("student with id: $studentId is not found")
        }

        val societyNameList: MutableList<String> =
            student.studentRoleEntities.map { studentRoleEntity: StudentRoleEntity -> studentRoleEntity.societyEntity.name }
                .toMutableList()

        societyNameList.forEach { societyName -> jwtUtil.verifyUserMemberRoleOfSociety(jwtToken, societyName) }

        return eventRepository.findAllBySocietyNameListOrderByApplyDeadlineDesc(
            societyNameList,
            PageRequest.of(pageNum, pageSize)
        )
            .map { eventEntity: EventEntity ->
                EventDto().createFromEntity(eventEntity, s3BucketDomain)
            }
    }

    fun getStudentSocietyStatus(itsc: String): List<StudentEnrolledSocietyStatusDto> {
        val studentEntity: StudentEntity = studentRepository.findByItsc(itsc).orElseThrow {
            Exception("student with itsc: $itsc is not exist")
        }
        return studentEntity.enrolledSocietyRecordEntities.map { enrolledSocietyRecordEntity: EnrolledSocietyRecordEntity ->
            StudentEnrolledSocietyStatusDto(
                societyName = enrolledSocietyRecordEntity.societyEntity.name,
                registerDate = dateUtil.convertLocalDateTimeToString(enrolledSocietyRecordEntity.createdAt),
                enrolledStatus = enrolledSocietyRecordEntity.status
            )
        }
    }

    fun countAllEventWithSocietyMember(jwtToken: String, studentId: String): TotalCountDto {
        val student: StudentEntity = studentRepository.findById(UUID.fromString(studentId)).orElseThrow {
            Exception("student with id: $studentId is not found")
        }

        val societyNameList: MutableList<String> =
            student.studentRoleEntities.map { studentRoleEntity: StudentRoleEntity -> studentRoleEntity.societyEntity.name }
                .toMutableList()

        societyNameList.forEach { societyName -> jwtUtil.verifyUserMemberRoleOfSociety(jwtToken, societyName) }

        return TotalCountDto(eventRepository.countBySocietyNameList(societyNameList))
    }
}