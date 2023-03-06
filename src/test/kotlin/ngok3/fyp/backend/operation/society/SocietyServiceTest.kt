package ngok3.fyp.backend.operation.society

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ngok3.fyp.backend.authentication.role.Role
import ngok3.fyp.backend.authentication.role.RoleEntity
import ngok3.fyp.backend.authentication.role.RoleEntityRepository
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordRepository
import ngok3.fyp.backend.operation.student.StudentDto
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class SocietyServiceTest {
    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()

    private val societyRepository: SocietyRepository = mockk(relaxed = true)
    private val studentRepository: StudentRepository = mockk(relaxed = true)
    private val enrolledSocietyRecordRepository: EnrolledSocietyRecordRepository = mockk()

    private val roleEntityRepository: RoleEntityRepository = mockk(relaxed = true)

    private val societyService: SocietyService = SocietyService(
        societyRepository = societyRepository,
        studentRepository = studentRepository,
        enrolledSocietyRecordRepository = enrolledSocietyRecordRepository,
        roleEntityRepository = roleEntityRepository
    )

    @Test
    fun getAllSocietyMember() {
        every {
            studentRepository.findByEnrolledSocietyName(mockAuthRepository.testSocietyName)
        } returns listOf(
            StudentEntity("qwerty", "nickname 1"),
            StudentEntity("asdfg", "nickname 2"),
        )

        val allMembers: List<StudentDto> = societyService.getAllSocietyMember(mockAuthRepository.testSocietyName)

        assertEquals("qwerty", allMembers[0].itsc)
        assertEquals("nickname 1", allMembers[0].nickname)

        assertEquals("asdfg", allMembers[1].itsc)
        assertEquals("nickname 2", allMembers[1].nickname)
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

        every { studentRepository.saveAll(mockStudentEntityList) } returns mockStudentEntityList

        societyService.assignSocietyMemberRole(mockAuthRepository.testSocietyName, studentIdList)

        verify(exactly = 1) { studentRepository.saveAll(mockStudentEntityList) }
    }

    @Test
    fun removeSocietyMemberRole() {
        val studentIdList: List<String> = listOf<String>(
            "2ac23d21-4cb0-4173-a2fe-de551ec5aa9d",
            "38153605-ed2c-42e7-947a-9d1731f4bd44"
        )

        val mockStudentEntityList: List<StudentEntity> = listOf(
            StudentEntity("qwert", "nickname 1"),
            StudentEntity("asdfg", "nickname 2"),
        )

        every {
            studentRepository.findByIdInAndEnrolledSocietyNameAndEnrollStatus(studentIdList.map { studentIdString ->
                UUID.fromString(
                    studentIdString
                )
            }.toMutableList(), mockAuthRepository.testSocietyName, EnrolledStatus.SUCCESS)
        } returns mockStudentEntityList

        every { studentRepository.saveAll(mockStudentEntityList) } returns mockStudentEntityList

        societyService.removeSocietyMemberRole(mockAuthRepository.testSocietyName, studentIdList)

        verify(exactly = 1) { studentRepository.saveAll(mockStudentEntityList) }
    }
}