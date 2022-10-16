package ngok3.fyp.backend.student

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.util.*

class StudentServiceTest {
    private val studentRepository: StudentRepository = mockk()
    private val studentService: StudentService = StudentService(studentRepository)

    @Test
    fun testGetStudentProfile() {
        //create mock student entity
        val mockStudentEntity: StudentEntity = StudentEntity("test_itsc", "test_name", "test_mail", "test_role")

        //mock db operation
        every { studentRepository.findByItsc("test_itsc") } returns Optional.of(mockStudentEntity)

        //mock service operation
        val mockStudentDto: StudentDto = studentService.getStudentProfile("test_itsc")

        //test value
        assertEquals(mockStudentDto.itsc, mockStudentEntity.itsc)
        assertEquals(mockStudentDto.nickname, mockStudentEntity.nickname)
        assertEquals(mockStudentDto.mail, mockStudentEntity.mail)
        assertEquals(mockStudentDto.role, mockStudentEntity.role)
    }
}