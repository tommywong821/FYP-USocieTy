package ngok3.fyp.backend.operation.society

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ngok3.fyp.backend.authentication.role.Role
import ngok3.fyp.backend.authentication.role.RoleEntity
import ngok3.fyp.backend.authentication.role.RoleEntityRepository
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntity
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntityRepository
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordEntityRepository
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentEntityRepository
import ngok3.fyp.backend.operation.student.model.StudentDto
import ngok3.fyp.backend.util.JWTUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class SocietyServiceTest {
    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()

    private val societyRepository: SocietyEntityRepository = mockk(relaxed = true)
    private val studentRepository: StudentEntityRepository = mockk(relaxed = true)
    private val enrolledSocietyRecordRepository: EnrolledSocietyRecordEntityRepository = mockk()

    private val roleEntityRepository: RoleEntityRepository = mockk(relaxed = true)
    private val studentRoleEntityRepository: StudentRoleEntityRepository = mockk(relaxed = true)
    private val jwtUtil: JWTUtil = JWTUtil(studentRoleEntityRepository = studentRoleEntityRepository)


    private val societyService: SocietyService = SocietyService(
        societyRepository = societyRepository,
        studentRepository = studentRepository,
        enrolledSocietyRecordRepository = enrolledSocietyRecordRepository,
        roleEntityRepository = roleEntityRepository,
        studentRoleEntityRepository = studentRoleEntityRepository,
        jwtUtil = jwtUtil
    )

    @Test
    fun getAllSocietyMember() {
        val societyEntity: SocietyEntity = SocietyEntity(name = "test society 1")
        val student1: StudentEntity = StudentEntity(itsc = "qwerty", nickname = "nickname 1")
        val studentRoleEntity: StudentRoleEntity = StudentRoleEntity()
        studentRoleEntity.societyEntity = societyEntity
        student1.studentRoleEntities = mutableSetOf(studentRoleEntity)

        val student2: StudentEntity = StudentEntity(itsc = "asdfg", nickname = "nickname 2")

        every {
            studentRepository.findByEnrolledSocietyRecordEntities_StatusNotInAndEnrolledSocietyRecordEntities_SocietyEntity_Name(
                EnrolledStatus.PENDING,
                mockAuthRepository.testSocietyName
            )
        } returns listOf(
            student1,
            student2
        )

        val allMembers: List<StudentDto> = societyService.getAllSocietyMember(mockAuthRepository.testSocietyName)

        assertEquals("qwerty", allMembers[0].itsc)
        assertEquals("nickname 1", allMembers[0].nickname)
        assertEquals(arrayListOf<String>("test society 1"), allMembers[0].roles)

        assertEquals("asdfg", allMembers[1].itsc)
        assertEquals("nickname 2", allMembers[1].nickname)
        assertEquals(arrayListOf<String>(), allMembers[1].roles)
    }

    @Test
    fun assignSocietyMemberRole() {
        val studentIdList: List<String> = listOf<String>(
            "2ac23d21-4cb0-4173-a2fe-de551ec5aa9d",
            "38153605-ed2c-42e7-947a-9d1731f4bd44"
        )

        val mockStudentEntityList: List<StudentEntity> = listOf(
            StudentEntity("qwert", "nickname 1"),
            StudentEntity("asdfg", "nickname 2"),
        )

        every {
            roleEntityRepository.findByRole(Role.ROLE_SOCIETY_MEMBER)
        } returns Optional.of(RoleEntity(1, Role.ROLE_SOCIETY_MEMBER))

        every {
            societyRepository.findByName(mockAuthRepository.testSocietyName)
        } returns Optional.of(SocietyEntity())

        every {
            studentRepository.findByIdInAndEnrolledSocietyNameAndEnrollStatus(studentIdList.map { studentIdString ->
                UUID.fromString(
                    studentIdString
                )
            }.toMutableList(), mockAuthRepository.testSocietyName, EnrolledStatus.SUCCESS)
        } returns mockStudentEntityList


        societyService.assignSocietyMemberRole(mockAuthRepository.testSocietyName, studentIdList)

    }

    @Test
    fun removeSocietyMemberRole() {
        val studentIdList: List<String> = listOf<String>(
            "2ac23d21-4cb0-4173-a2fe-de551ec5aa9d",
            "38153605-ed2c-42e7-947a-9d1731f4bd44"
        )

        val mockStudentRoleEntityList: List<StudentRoleEntity> = listOf(
            StudentRoleEntity()
        )

        every {
            studentRoleEntityRepository.findByStudentEntity_UuidInAndSocietyEntity_Name(studentIdList.map { studentIdString ->
                UUID.fromString(
                    studentIdString
                )
            }.toMutableList(), mockAuthRepository.testSocietyName)
        } returns mockStudentRoleEntityList

        every { studentRoleEntityRepository.deleteAll(mockStudentRoleEntityList) } returns Unit

        societyService.removeSocietyMemberRole(mockAuthRepository.testSocietyName, studentIdList)

        verify(exactly = 1) { studentRoleEntityRepository.deleteAll(mockStudentRoleEntityList) }
    }

//    TODO dummy remove
//    @Test
//    fun getTotalNumberOfHoldingEvent() {
//        val uuid: String = UUID.randomUUID().toString()
//        val societyDto: SocietyDto = SocietyDto(
//            id = uuid,
//            name = mockAuthRepository.testSocietyName,
//            description = "description",
//            holdingEventNumber = 10
//        )
//
//        every {
//            societyRepository.findHoldingEventNumberOfSociety(any())
//        } returns listOf(societyDto)
//
//        val result: List<SocietyDto> = societyService.getTotalNumberOfHoldingEvent()
//
//        assertEquals(societyDto.id, result[0].id)
//        assertEquals(societyDto.name, result[0].name)
//        assertEquals(societyDto.description, result[0].description)
//        assertEquals(societyDto.holdingEventNumber, result[0].holdingEventNumber)
//    }
}