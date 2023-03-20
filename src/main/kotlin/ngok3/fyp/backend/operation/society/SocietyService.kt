package ngok3.fyp.backend.operation.society

import ngok3.fyp.backend.authentication.role.Role
import ngok3.fyp.backend.authentication.role.RoleEntity
import ngok3.fyp.backend.authentication.role.RoleEntityRepository
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntity
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntityRepository
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordEntity
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordKey
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordRepository
import ngok3.fyp.backend.operation.student.StudentDto
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
    private val enrolledSocietyRecordRepository: EnrolledSocietyRecordRepository,
    private val roleEntityRepository: RoleEntityRepository,
    private val studentRoleEntityRepository: StudentRoleEntityRepository
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

    fun joinSociety(itsc: String, societyName: String): Boolean {
        val studentEntity: StudentEntity = studentRepository.findByItsc(itsc).orElseThrow {
            throw Exception("student with itsc:$itsc is not found")
        }


        val societyEntity: SocietyEntity = societyRepository.findByName(societyName).orElseThrow {
            Exception("Society with id:$societyName is not found")
        }

        val enrolledSocietyRecordEntity = EnrolledSocietyRecordEntity(
            id = EnrolledSocietyRecordKey(studentEntity.uuid, societyEntity.uuid), status = EnrolledStatus.PENDING
        )
        enrolledSocietyRecordEntity.studentEntity = studentEntity
        enrolledSocietyRecordEntity.societyEntity = societyEntity
        enrolledSocietyRecordRepository.save(enrolledSocietyRecordEntity)
        return true;
    }

    fun getAllSocietyMember(societyName: String): List<StudentDto> {
        return studentRepository.findAllStudentEnrolledInSocietyButNotSocietyMember(
            societyName,
            EnrolledStatus.SUCCESS,
            societyName
        ).map { studentEntity: StudentEntity ->
            StudentDto(
                studentEntity.uuid.toString(),
                studentEntity.itsc,
                studentEntity.nickname,
                studentEntity.mail,
                emptyList(),
                emptyList()
            )
        }
    }

    fun assignSocietyMemberRole(societyName: String, studentIdList: List<String>) {
        val roleEntity: RoleEntity = roleEntityRepository.findByRole(Role.ROLE_SOCIETY_MEMBER).orElseThrow {
            Exception("Role: ${Role.ROLE_SOCIETY_MEMBER} does not exist")
        }

        val studentEntityList: Iterable<StudentEntity> =
            studentRepository.findByIdInAndEnrolledSocietyNameAndEnrollStatus(studentIdList.map { studentIdString ->
                UUID.fromString(studentIdString)
            }.toMutableList(), societyName, EnrolledStatus.SUCCESS)

        val societyEntity: SocietyEntity = societyRepository.findByName(societyName).orElseThrow {
            Exception("Society: $societyName does not exist")
        }

        val studentRoleEntityList: ArrayList<StudentRoleEntity> = ArrayList()

        for (studentEntity in studentEntityList) {
            val studentRoleEntity: StudentRoleEntity = StudentRoleEntity()
            studentRoleEntity.roleEntity = roleEntity
            studentRoleEntity.studentEntity = studentEntity
            studentRoleEntity.societyEntity = societyEntity
            studentRoleEntityList.add(studentRoleEntity)
        }
        studentRoleEntityRepository.saveAll(studentRoleEntityList)
    }

    fun removeSocietyMemberRole(societyName: String, studentIdList: List<String>) {
        val studentEntityList: Iterable<StudentEntity> =
            studentRepository.findByIdInAndEnrolledSocietyNameAndEnrollStatus(studentIdList.map { studentIdString ->
                UUID.fromString(studentIdString)
            }.toMutableList(), societyName, EnrolledStatus.SUCCESS)

        studentEntityList.forEach { studentEntity -> studentEntity.studentRoleEntities.removeIf { studentRoleEntity -> Role.ROLE_SOCIETY_MEMBER == studentRoleEntity.roleEntity.role } }

        studentRepository.saveAll(studentEntityList)
    }
}