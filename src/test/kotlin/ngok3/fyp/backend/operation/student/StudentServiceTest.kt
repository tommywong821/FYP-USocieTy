package ngok3.fyp.backend.operation.student

import io.mockk.every
import io.mockk.mockk
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntity
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntityRepository
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordEntity
import ngok3.fyp.backend.operation.event.EventRepository
import ngok3.fyp.backend.operation.society.SocietyEntity
import ngok3.fyp.backend.util.DateUtil
import ngok3.fyp.backend.util.JWTUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.*

class StudentServiceTest() {
    private val mockStudentRepository: MockStudentRepository = MockStudentRepository()
    private val studentRepository: StudentRepository = mockk()
    private val eventRepository: EventRepository = mockk()
    private val studentRoleEntityRepository: StudentRoleEntityRepository = mockk()

    private val jwtUtil: JWTUtil = JWTUtil(studentRoleEntityRepository = studentRoleEntityRepository)
    private val dateUtil: DateUtil = DateUtil()

    private val studentService: StudentService =
        StudentService(
            studentRepository = studentRepository,
            jwtUtil = jwtUtil,
            eventRepository = eventRepository,
            dateUtil = dateUtil
        )

    @Test
    fun `should get test student profile with itsc`() {
        //create mock student entity
        val mockStudentEntity: StudentEntity = mockStudentRepository.testStudentEntity
        val mockStudentEntityItsc: String = mockStudentRepository.testItsc

        //mock db operation
        every { studentRepository.findByItsc(mockStudentEntityItsc) } returns Optional.of(mockStudentEntity)

        //mock service operation
        val mockStudentDto: StudentDto = studentService.getStudentProfile(mockStudentEntityItsc, "", "")

        //test value
        assertEquals(mockStudentDto.itsc, mockStudentEntity.itsc)
        assertEquals(mockStudentDto.nickname, mockStudentEntity.nickname)
        assertEquals(mockStudentDto.mail, mockStudentEntity.mail)
    }

    @Test
    fun `should get test student profile with uuid`() {
        //create mock student entity
        val mockStudentEntity: StudentEntity = mockStudentRepository.testStudentEntity
        val mockStudentEntityItsc: String = mockStudentRepository.testItsc
        val mockUuid: String = UUID.randomUUID().toString()

        //mock db operation
        every { studentRepository.findById(UUID.fromString(mockUuid)) } returns Optional.of(mockStudentEntity)

        //mock service operation
        val mockStudentDto: StudentDto = studentService.getStudentProfile("", mockUuid, "")

        //test value
        assertEquals(mockStudentDto.itsc, mockStudentEntity.itsc)
        assertEquals(mockStudentDto.nickname, mockStudentEntity.nickname)
        assertEquals(mockStudentDto.mail, mockStudentEntity.mail)
    }

    @Test
    fun `should get test student profile with cardId`() {
        //create mock student entity
        val mockStudentEntity: StudentEntity = mockStudentRepository.testStudentEntity
        val mockStudentEntityItsc: String = mockStudentRepository.testItsc
        val mockUuid: String = UUID.randomUUID().toString()

        //mock db operation
        every { studentRepository.findByCardId("cardId") } returns Optional.of(mockStudentEntity)

        //mock service operation
        val mockStudentDto: StudentDto = studentService.getStudentProfile("", "", "cardId")

        //test value
        assertEquals(mockStudentDto.itsc, mockStudentEntity.itsc)
        assertEquals(mockStudentDto.nickname, mockStudentEntity.nickname)
        assertEquals(mockStudentDto.mail, mockStudentEntity.mail)
    }

    @Test
    fun getStudentSocietyStatus() {
        // Given
        val itsc = "12345678"
        val studentEntity = StudentEntity("John Doe", itsc)

        val societyEntityA = SocietyEntity("Society A")
        val registerDate1 = LocalDateTime.now()
        val enrolledSocietyRecord1 =
            EnrolledSocietyRecordEntity(status = EnrolledStatus.SUCCESS, createdAt = registerDate1)
        enrolledSocietyRecord1.societyEntity = societyEntityA

        val societyEntityB = SocietyEntity("Society B")
        val registerDate2 = LocalDateTime.now().minusDays(1)
        val enrolledSocietyRecord2 =
            EnrolledSocietyRecordEntity(status = EnrolledStatus.PENDING, createdAt = registerDate2)
        enrolledSocietyRecord2.societyEntity = societyEntityB
        studentEntity.enrolledSocietyRecordEntities = mutableSetOf(
            enrolledSocietyRecord1,
            enrolledSocietyRecord2
        )
        every { (studentRepository.findByItsc(itsc)) } returns (Optional.of(studentEntity))

        // When
        val result = studentService.getStudentSocietyStatus(itsc)

        // Then
        assertEquals(2, result.size)
        assertEquals("Society A", result[0].societyName)
        assertEquals(dateUtil.convertLocalDateTimeToStringWithTime(registerDate1), result[0].registerDate)
        assertEquals("Society B", result[1].societyName)
        assertEquals(dateUtil.convertLocalDateTimeToStringWithTime(registerDate2), result[1].registerDate)
        assertEquals(EnrolledStatus.SUCCESS, result[0].enrolledStatus)
        assertEquals(EnrolledStatus.PENDING, result[1].enrolledStatus)

    }

    @Test
    fun countAllEventWithSocietyMember() {
        val studentEntity: StudentEntity = StudentEntity()
        val societyEntity: SocietyEntity = SocietyEntity("society 1")
        val studentRoleEntity: StudentRoleEntity = StudentRoleEntity()
        studentRoleEntity.societyEntity = societyEntity
        studentEntity.studentRoleEntities = mutableSetOf(
            studentRoleEntity
        )

        every { studentRepository.findById(any()) } returns Optional.of(studentEntity)

        every { eventRepository.countBySocietyNameList(any()) } returns 23

        val result = studentService.countAllEventWithSocietyMember(UUID.randomUUID().toString())

        assertEquals(23, result.totalNumber)
    }
}