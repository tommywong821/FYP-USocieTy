package ngok3.fyp.backend.operation.enrolledSociety

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
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

    private val jwtUtil: JWTUtil = JWTUtil(enrolledSocietyRecordRepository = enrolledSocietyRecordRepository)

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
            enrolledSocietyRecordRepository.findByItscAndSocietyNameAndEnrolledStatus(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                EnrolledStatus.SUCCESS
            )
        } returns Optional.of(EnrolledSocietyRecordEntity())

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
            enrolledSocietyRecordRepository.findByItscAndSocietyNameAndEnrolledStatus(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                EnrolledStatus.SUCCESS
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

        val mockStudentListNotInSuccess: List<StudentEnrolledEventRecord> = listOf(
            StudentEnrolledEventRecord("qwert", "nickname 1", EnrolledStatus.PENDING),
            StudentEnrolledEventRecord("asdfg", "nickname 2", EnrolledStatus.DECLINE),
        )

        every {
            enrolledSocietyRecordRepository.findByItscAndSocietyNameAndEnrolledStatus(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                EnrolledStatus.SUCCESS
            )
        } returns Optional.of(EnrolledSocietyRecordEntity())

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