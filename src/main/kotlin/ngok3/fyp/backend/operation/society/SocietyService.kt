package ngok3.fyp.backend.operation.society

import ngok3.fyp.backend.authentication.role.Role
import ngok3.fyp.backend.authentication.role.RoleEntity
import ngok3.fyp.backend.authentication.role.RoleEntityRepository
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntity
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntityRepository
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordEntity
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordEntityRepository
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordKey
import ngok3.fyp.backend.operation.society.model.SocietyDto
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentEntityRepository
import ngok3.fyp.backend.operation.student.model.StudentDto
import ngok3.fyp.backend.util.JWTUtil
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.util.*

@Service
class SocietyService(
    private val societyEntityRepository: SocietyEntityRepository,
    private val studentRepository: StudentEntityRepository,
    private val enrolledSocietyRecordRepository: EnrolledSocietyRecordEntityRepository,
    private val roleEntityRepository: RoleEntityRepository,
    private val studentRoleEntityRepository: StudentRoleEntityRepository,
    private val jwtUtil: JWTUtil
) {
    fun getAllSocieties(pageNum: Int, pageSize: Int): List<SocietyDto> {
        val firstPageNumWithPageSizeElement: Pageable = PageRequest.of(pageNum, pageSize)

        return societyEntityRepository.findByOrderByNameAsc(firstPageNumWithPageSizeElement).content.map { societyEntity ->
            SocietyDto(
                societyEntity
            )
        }
    }

    fun joinSociety(itsc: String, societyName: String): Boolean {
        val studentEntity: StudentEntity = studentRepository.findByItsc(itsc).orElseThrow {
            throw Exception("student with itsc:$itsc is not found")
        }


        val societyEntity: SocietyEntity = societyEntityRepository.findByName(societyName).orElseThrow {
            Exception("Society with id:$societyName is not found")
        }

        val enrolledSocietyRecordKey: EnrolledSocietyRecordKey =
            EnrolledSocietyRecordKey(studentEntity.uuid, societyEntity.uuid)
        if (enrolledSocietyRecordRepository.findById(enrolledSocietyRecordKey).isPresent) {
            throw DuplicateKeyException("Student: $itsc is already join society: $societyName")
        }

        val enrolledSocietyRecordEntity = EnrolledSocietyRecordEntity(
            id = enrolledSocietyRecordKey, status = EnrolledStatus.PENDING
        )
        enrolledSocietyRecordEntity.studentEntity = studentEntity
        enrolledSocietyRecordEntity.societyEntity = societyEntity
        enrolledSocietyRecordRepository.save(enrolledSocietyRecordEntity)
        return true
    }

    fun getAllSocietyMember(jwtToken: String, societyName: String): List<StudentDto> {
        jwtUtil.verifyUserMemberRoleOfSociety(jwtToken, societyName)

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

    fun assignSocietyMemberRole(jwtToken: String, societyName: String, studentIdList: List<String>) {
        jwtUtil.verifyUserMemberRoleOfSociety(jwtToken, societyName)
        val roleEntity: RoleEntity = roleEntityRepository.findByRole(Role.ROLE_SOCIETY_MEMBER).orElseThrow {
            Exception("Role: ${Role.ROLE_SOCIETY_MEMBER} does not exist")
        }

        val studentEntityList: Iterable<StudentEntity> =
            studentRepository.findByIdInAndEnrolledSocietyNameAndEnrollStatus(studentIdList.map { studentIdString ->
                UUID.fromString(studentIdString)
            }.toMutableList(), societyName, EnrolledStatus.SUCCESS)

        val societyEntity: SocietyEntity = societyEntityRepository.findByName(societyName).orElseThrow {
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

    fun removeSocietyMemberRole(jwtToken: String, societyName: String, studentIdList: List<String>) {
        jwtUtil.verifyUserMemberRoleOfSociety(jwtToken, societyName)
        val studentRoleEntityList: List<StudentRoleEntity> =
            studentRoleEntityRepository.findByStudentEntity_UuidInAndSocietyEntity_Name(studentIdList.map { s: String ->
                UUID.fromString(s)
            }.toMutableList(), societyName)

        studentRoleEntityRepository.deleteAll(studentRoleEntityList)
    }

    fun getAllSocietiesWithSocietyMemberRole(jwtToken: String, pageNum: Int, pageSize: Int): List<SocietyDto> {
        val societyNameString: String = jwtUtil.getClaimFromJWTToken(jwtToken, "role")
        val societyNameList: MutableList<String> = societyNameString
            .replace("[", "") // remove the opening square bracket
            .replace("]", "") // remove the closing square bracket
            .split(", ").toMutableList()
        return societyEntityRepository.findByNameIn(societyNameList, PageRequest.of(pageNum, pageSize))
            .map { societyEntity ->
                SocietyDto(
                    societyEntity
                )
            }
    }

    fun removeFromSociety(jwtToken: String, societyName: String, studentIdList: List<String>) {
        jwtUtil.verifyUserMemberRoleOfSociety(jwtToken = jwtToken, societyName = societyName)

        val societyEntity: SocietyEntity = societyEntityRepository.findByName(societyName).orElseThrow {
            Exception("society: $societyName is not exist")
        }

        enrolledSocietyRecordRepository.deleteAllById(studentIdList.map { studentId ->
            EnrolledSocietyRecordKey(
                societyUuid = societyEntity.uuid,
                studentUuid = UUID.fromString(studentId)
            )
        })
    }

//    TODO dummy remove
//    fun getTotalNumberOfHoldingEvent(): List<SocietyDto> {
//        return societyRepository.findHoldingEventNumberOfSociety(LocalDateTime.now())
//    }
}