package ngok3.fyp.backend.operation.student

import io.mockk.every
import io.mockk.mockk
import ngok3.fyp.backend.authentication.student_role.StudentRoleEntityRepository
import ngok3.fyp.backend.operation.event.EventRepository
import ngok3.fyp.backend.util.JWTUtil
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class StudentServiceTest() {
    private val mockStudentRepository: MockStudentRepository = MockStudentRepository()
    private val studentRepository: StudentRepository = mockk()
    private val eventRepository: EventRepository = mockk()
    private val studentRoleEntityRepository: StudentRoleEntityRepository = mockk()

    private val jwtUtil: JWTUtil = JWTUtil(studentRoleEntityRepository = studentRoleEntityRepository)

    private val studentService: StudentService =
        StudentService(studentRepository = studentRepository, jwtUtil = jwtUtil, eventRepository = eventRepository)

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
}