package ngok3.fyp.backend.student

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class StudentServiceTest() {
    private val mockStudentRepository: MockStudentRepository = MockStudentRepository()
    private val studentRepository: StudentRepository = mockk()
    private val studentService: StudentService = StudentService(studentRepository)

    @Test
    fun `should get test student profile`() {
        //create mock student entity
        val mockStudentEntity: StudentEntity = mockStudentRepository.testStudentEntity
        val mockStudentEntityItsc: String = mockStudentRepository.testItsc

        //mock db operation
        every { studentRepository.findByItsc(mockStudentEntityItsc) } returns Optional.of(mockStudentEntity)

        //mock service operation
        val mockStudentDto: StudentDto = studentService.getStudentProfile(mockStudentEntityItsc)

        //test value
        assertEquals(mockStudentDto.itsc, mockStudentEntity.itsc)
        assertEquals(mockStudentDto.nickname, mockStudentEntity.nickname)
        assertEquals(mockStudentDto.mail, mockStudentEntity.mail)
        assertEquals(mockStudentDto.role, mockStudentEntity.role)
    }
}