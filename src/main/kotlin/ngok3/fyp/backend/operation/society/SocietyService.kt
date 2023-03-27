package ngok3.fyp.backend.operation.society

import ngok3.fyp.backend.authentication.role.Role
import ngok3.fyp.backend.authentication.role.RoleEntity
import ngok3.fyp.backend.authentication.role.RoleEntityRepository
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntity
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntityRepository
import ngok3.fyp.backend.operation.TotalCountDto
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordEntity
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordKey
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordRepository
import ngok3.fyp.backend.operation.student.StudentDto
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentRepository
import ngok3.fyp.backend.util.JWTUtil
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class SocietyService(
    private val societyRepository: SocietyRepository,
    private val studentRepository: StudentRepository,
    private val enrolledSocietyRecordRepository: EnrolledSocietyRecordRepository,
    private val roleEntityRepository: RoleEntityRepository,
    private val studentRoleEntityRepository: StudentRoleEntityRepository,
    private val jwtUtil: JWTUtil
) {
    fun getAllSocieties(pageNum: Int, pageSize: Int): List<SocietyDto> {
        val firstPageNumWithPageSizeElement: Pageable = PageRequest.of(pageNum, pageSize)

        return societyRepository.findByOrderByNameAsc(firstPageNumWithPageSizeElement).content.map { societyEntity ->
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
        return true
    }

    fun getAllSocietyMember(societyName: String): List<StudentDto> {
        val allStudentList =
            studentRepository.findByEnrolledSocietyRecordEntities_StatusNotInAndEnrolledSocietyRecordEntities_SocietyEntity_Name(
                EnrolledStatus.PENDING,
                societyName
            ).toMutableList()

        return allStudentList.map { studentEntity: StudentEntity ->
            StudentDto(
                studentEntity = studentEntity,
                enrolledSocietyList = emptyList(),
                roles = studentEntity.studentRoleEntities.map { studentRoleEntity: StudentRoleEntity -> studentRoleEntity.societyEntity.name })
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

    fun getAllSocietiesWithSocietyMemberRole(jwtToken: String, pageNum: Int, pageSize: Int): List<SocietyDto> {
        val societyNameString: String = jwtUtil.getClaimFromJWTToken(jwtToken, "role")
        val societyNameList: MutableList<String> = societyNameString
            .replace("[", "") // remove the opening square bracket
            .replace("]", "") // remove the closing square bracket
            .split(", ").toMutableList()
        return societyRepository.findByNameIn(societyNameList, PageRequest.of(pageNum, pageSize)).map { societyEntity ->
            SocietyDto(
                societyEntity
            )
        }
    }

    fun getTotalNumberOfHoldingEvent(societyName: String): TotalCountDto {
        return TotalCountDto(societyRepository.countBySocietyNameAndApplyDeadline(societyName, LocalDateTime.now()))
    }
}