package ngok3.fyp.backend.operation.enrolledEvent

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordEntity
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordKey
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordRepository
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordService
import ngok3.fyp.backend.operation.enrolled.event_record.model.UpdateEnrolledEventRecordDto
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordEntity
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordRepository
import ngok3.fyp.backend.util.JWTUtil
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import java.util.*

@SpringBootTest
class EnrolledEventRecordServiceTest {
    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()
    private val enrolledSocietyRecordRepository: EnrolledSocietyRecordRepository = mockk()
    private val enrolledEventRecordRepository: EnrolledEventRecordRepository = mockk()

    private val jwtUtil: JWTUtil = JWTUtil(enrolledSocietyRecordRepository = enrolledSocietyRecordRepository)

    private val enrolledEventService: EnrolledEventRecordService = EnrolledEventRecordService(
        enrolledEventRecordRepository = enrolledEventRecordRepository,
        jwtUtil = jwtUtil
    )

    @Test
    fun `should update enrolled event record`() {
        val eventUUID: UUID = UUID.randomUUID()
        val studentUUID: UUID = UUID.randomUUID()
        val mockEnrolledEventRecordEntity: EnrolledEventRecordEntity = EnrolledEventRecordEntity(
            id = EnrolledEventRecordKey(studentUuid = studentUUID, eventUuid = eventUUID)
        )
        mockEnrolledEventRecordEntity.eventEntity.societyEntity.name = mockAuthRepository.testSocietyName

        every {
            enrolledEventRecordRepository.findById(
                EnrolledEventRecordKey(
                    studentUuid = studentUUID,
                    eventUuid = eventUUID,
                )
            )
        } returns Optional.of(mockEnrolledEventRecordEntity)

        every {
            enrolledSocietyRecordRepository.findByItscAndSocietyNameAndEnrolledStatus(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                EnrolledStatus.SUCCESS
            )
        } returns Optional.of(EnrolledSocietyRecordEntity())

        every {
            enrolledEventRecordRepository.save(mockEnrolledEventRecordEntity)
        } returns mockEnrolledEventRecordEntity

        enrolledEventService.updateEnrolledEventRecord(
            mockAuthRepository.validUserCookieToken,
            UpdateEnrolledEventRecordDto(
                eventId = eventUUID.toString(),
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
            enrolledEventRecordRepository.findById(
                EnrolledEventRecordKey(
                    studentUuid = studentUUID,
                    eventUuid = eventUUID,
                )
            )
        }
        verify(exactly = 1) {
            enrolledEventRecordRepository.save(mockEnrolledEventRecordEntity)
        }
    }
}