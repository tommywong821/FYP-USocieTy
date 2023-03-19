package ngok3.fyp.backend.operation.enrolledEvent

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ngok3.fyp.backend.authentication.role.Role
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntity
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntityRepository
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.event_record.*
import ngok3.fyp.backend.operation.enrolled.event_record.model.StudentEnrolledEventRecordDto
import ngok3.fyp.backend.operation.enrolled.event_record.model.UpdateEnrolledEventRecordDto
import ngok3.fyp.backend.operation.event.EventEntity
import ngok3.fyp.backend.operation.event.EventRepository
import ngok3.fyp.backend.operation.society.SocietyEntity
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.util.JWTUtil
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import java.util.*


@SpringBootTest
class EnrolledEventRecordServiceTest {
    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()
    private val enrolledEventRecordRepository: EnrolledEventRecordRepository = mockk()
    private val studentRoleEntityRepository: StudentRoleEntityRepository = mockk()
    private val eventRepository: EventRepository = mockk()

    private val jwtUtil: JWTUtil = JWTUtil(studentRoleEntityRepository = studentRoleEntityRepository)

    private val enrolledEventService: EnrolledEventRecordService = EnrolledEventRecordService(
        enrolledEventRecordRepository = enrolledEventRecordRepository,
        jwtUtil = jwtUtil,
        eventRepository = eventRepository
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
            enrolledEventRecordRepository.findAllById(
                listOf(
                    EnrolledEventRecordKey(
                        studentUuid = studentUUID,
                        eventUuid = eventUUID,
                    )
                )
            )
        } returns listOf(mockEnrolledEventRecordEntity)

        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.of(StudentRoleEntity())

        every {
            enrolledEventRecordRepository.saveAll(listOf(mockEnrolledEventRecordEntity))
        } returns listOf(mockEnrolledEventRecordEntity)

        enrolledEventService.updateEnrolledEventRecord(
            mockAuthRepository.validUserCookieToken,
            listOf(
                UpdateEnrolledEventRecordDto(
                    eventId = eventUUID.toString(),
                    studentId = studentUUID.toString(),
                    status = EnrolledStatus.SUCCESS
                )
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
            enrolledEventRecordRepository.findAllById(
                listOf(
                    EnrolledEventRecordKey(
                        studentUuid = studentUUID,
                        eventUuid = eventUUID,
                    )
                )
            )
        }
        verify(exactly = 1) {
            enrolledEventRecordRepository.saveAll(listOf(mockEnrolledEventRecordEntity))
        }
    }

    @Test
    fun `should return all student with enrolled status with event id`() {
        val eventUuid: String = UUID.randomUUID().toString()
        val student1Id = UUID.randomUUID().toString()
        val student2Id = UUID.randomUUID().toString()
        val studentEnrolledEventRecordDtoList: List<StudentEnrolledEventRecordDto> = listOf(
            StudentEnrolledEventRecordDto(
                studentId = student1Id,
                itsc = "qwerty",
                paymentStatus = PaymentStatus.PAID.status,
                enrolledStatus = EnrolledStatus.SUCCESS.status
            ),
            StudentEnrolledEventRecordDto(
                studentId = student2Id,
                itsc = "asdfg",
                paymentStatus = PaymentStatus.UNPAID.status,
                enrolledStatus = EnrolledStatus.DECLINE.status
            ),
        )

        val mockEventEntity: EventEntity = EventEntity()
        val mockSocietyEntity: SocietyEntity = SocietyEntity()
        mockSocietyEntity.name = mockAuthRepository.testSocietyName
        mockEventEntity.societyEntity = mockSocietyEntity

        every {
            eventRepository.findById(UUID.fromString(eventUuid))
        } returns Optional.of(mockEventEntity)

        every {
            studentRoleEntityRepository.findByStudentItscAndSocietyNameAndRole(
                mockAuthRepository.validUserItsc,
                mockAuthRepository.testSocietyName,
                Role.ROLE_SOCIETY_MEMBER
            )
        } returns Optional.of(StudentRoleEntity())

        val mockStudent1 = StudentEntity(itsc = "qwerty")
        val mockStudent2 = StudentEntity(itsc = "asdfg")

        val mockEnrolledEventRecordEntity1 =
            EnrolledEventRecordEntity(paymentStatus = PaymentStatus.PAID, enrollStatus = EnrolledStatus.SUCCESS)
        mockEnrolledEventRecordEntity1.studentEntity = mockStudent1
        val mockEnrolledEventRecordEntity2 =
            EnrolledEventRecordEntity(paymentStatus = PaymentStatus.UNPAID, enrollStatus = EnrolledStatus.DECLINE)
        mockEnrolledEventRecordEntity2.studentEntity = mockStudent2

        val mockEntityList = listOf<EnrolledEventRecordEntity>(
            mockEnrolledEventRecordEntity1,
            mockEnrolledEventRecordEntity2
        )

        every {
            enrolledEventRecordRepository.findAllEnrolledRecordStudentByEventIdWithPaging(
                eventUuid = UUID.fromString(eventUuid),
                PageRequest.of(0, 10)
            )
        } returns mockEntityList

        val returnList: List<StudentEnrolledEventRecordDto> = enrolledEventService.getStudentEnrolledEventRecord(
            mockAuthRepository.validUserCookieToken,
            eventUuid,
            0,
            10
        )

        assertThat(returnList).usingRecursiveComparison().ignoringFields("studentId")
            .isEqualTo(studentEnrolledEventRecordDtoList)
    }
}