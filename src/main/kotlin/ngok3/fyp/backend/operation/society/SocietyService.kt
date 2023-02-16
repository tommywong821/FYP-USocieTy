package ngok3.fyp.backend.operation.society

import ngok3.fyp.backend.operation.enrolled_event_record.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled_society_record.EnrolledSocietyRecordEntity
import ngok3.fyp.backend.operation.enrolled_society_record.EnrolledSocietyRecordKey
import ngok3.fyp.backend.operation.enrolled_society_record.EnrolledSocietyRecordRepository
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class SocietyService(
    private val societyRepository: SocietyRepository,
    private val studentRepository: StudentRepository,
    private val enrolledSocietyRecordRepository: EnrolledSocietyRecordRepository
) {
    fun getAllSocieties(pageNum: Int, pageSize: Int): List<SocietyDto> {
        val firstPageNumWithPageSizeElement: Pageable = PageRequest.of(pageNum, pageSize)
        val allSocieties = societyRepository.findByOrderByNameAsc(firstPageNumWithPageSizeElement).content

        return allSocieties.map { societyEntity ->
            SocietyDto(
                societyEntity
            )
        }
    }

    fun joinSociety(itsc: String, societyId: String): Boolean {
        val studentEntityOptional: Optional<StudentEntity> = studentRepository.findByItsc(itsc)
        if (studentEntityOptional.isEmpty) {
            throw Exception("student with itsc:$itsc is not found")
        }

        val societyEntityOptional: Optional<SocietyEntity> = societyRepository.findById(UUID.fromString(societyId))
        if (societyEntityOptional.isEmpty) {
            throw Exception("Society with id:$societyId is not found")
        }

        val studentEntity: StudentEntity = studentEntityOptional.get()
        val societyEntity: SocietyEntity = societyEntityOptional.get()
        val enrolledSocietyRecordEntity = EnrolledSocietyRecordEntity(
            EnrolledSocietyRecordKey(studentEntity.uuid, societyEntity.uuid), EnrolledStatus.PENDING
        )
        enrolledSocietyRecordEntity.studentEntity = studentEntity
        enrolledSocietyRecordEntity.societyEntity = societyEntity
        enrolledSocietyRecordRepository.save(enrolledSocietyRecordEntity)
        return true;
    }
}