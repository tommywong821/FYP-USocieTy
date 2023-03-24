package ngok3.fyp.backend.operation.enrolledSociety

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ngok3.fyp.backend.authentication.role.Role
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntity
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntityRepository
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.society_record.*
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.util.JWTUtil
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class EnrolledSocietyRecordServiceTest {
    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()
    private val enrolledSocietyRecordRepository: EnrolledSocietyRecordRepository = mockk()
    private val studentRoleEntityRepository: StudentRoleEntityRepository = mockk()

    private val jwtUtil: JWTUtil = JWTUtil(studentRoleEntityRepository = studentRoleEntityRepository)

    private val enrolledSocietyService: EnrolledSocietyRecordService = EnrolledSocietyRecordService(
        enrolledSocietyRepository = enrolledSocietyRecordRepository,
        jwtUtil = jwtUtil
    )

    @Test
    fun `should update enrolled society record`() {
        val societyUUID: UUID = UUID.randomUUID()
        val studentUUID: UUID = UUID.randomUUID()
        val mockEnrolledSocietyRecordEntity: EnrolledSocietyRecordEntity = EnrolledSocietyRecordEntity(
            id = EnrolledSocietyRecordKey(studentUuid = studentUUID, societyUuid = societyUUID)
        )
        mockEnrolledSocietyRecordEntity.societyEntity.name = mockAuthRepository.testSocietyName

        every {
            enrolledSocietyRecordRepository.findById(
                EnrolledSocietyRecordKey(
                    studentUuid = studentUUID,
                    societyUuid = societyUUID,
                )
            )
        } returns Optional.of(mockEnrolledSocietyRecordEntity)

        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.of(StudentRoleEntity())

        every {
            enrolledSocietyRecordRepository.save(mockEnrolledSocietyRecordEntity)
        } returns mockEnrolledSocietyRecordEntity

        enrolledSocietyService.updateEnrolledSocietyRecord(
            mockAuthRepository.validUserCookieToken,
            UpdateEnrolledSocietyRecordDto(
                societyId = societyUUID.toString(),
                studentId = studentUUID.toString(),
                status = EnrolledStatus.SUCCESS
            )
        )

        verify(exactly = 1) {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        }
        verify(exactly = 1) {
            enrolledSocietyRecordRepository.findById(
                EnrolledSocietyRecordKey(
                    studentUuid = studentUUID,
                    societyUuid = societyUUID,
                )
            )
        }
        verify(exactly = 1) {
            enrolledSocietyRecordRepository.save(mockEnrolledSocietyRecordEntity)
        }
    }

    @Test
    fun `should get all student with enrolled society status not equal to success`() {
        val record1: EnrolledSocietyRecordEntity = EnrolledSocietyRecordEntity()
        record1.status = EnrolledStatus.PENDING
        val student1: StudentEntity = StudentEntity()
        student1.itsc = "qwert"
        student1.nickname = "nickname 1"
        record1.studentEntity = student1

        val record2: EnrolledSocietyRecordEntity = EnrolledSocietyRecordEntity()
        record1.status = EnrolledStatus.DECLINE
        val student2: StudentEntity = StudentEntity()
        student2.itsc = "asdfg"
        student2.nickname = "nickname 2"
        record2.studentEntity = student2

        val mockEnrolledDBResult = listOf<EnrolledSocietyRecordEntity>(
            record1, record2
        )

        val mockStudentListNotInSuccess: List<StudentEnrolledSocietyRecordDto> = listOf(
            StudentEnrolledSocietyRecordDto(
                studentId = "qwert",
                societyId = "",
                itsc = "qwert",
                name = "nickname 1",
                status = EnrolledStatus.PENDING
            ),
            StudentEnrolledSocietyRecordDto(
                studentId = "qwert",
                societyId = "",
                itsc = "asdfg",
                name = "nickname 2",
                status = EnrolledStatus.DECLINE
            ),
        )

        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.of(StudentRoleEntity())

        every {
            enrolledSocietyRecordRepository.findBySocietyEntity_NameAndStatusNotEqual(
                mockAuthRepository.testSocietyName,
                EnrolledStatus.SUCCESS
            )
        } returns mockEnrolledDBResult

        enrolledSocietyService.getEnrolledSocietyRecord(
            mockAuthRepository.validUserCookieToken,
            mockAuthRepository.testSocietyName
        )
    }
}