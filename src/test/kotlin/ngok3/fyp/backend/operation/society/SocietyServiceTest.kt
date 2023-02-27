package ngok3.fyp.backend.operation.society

import io.mockk.every
import io.mockk.mockk
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordRepository
import ngok3.fyp.backend.operation.student.StudentDto
import ngok3.fyp.backend.operation.student.StudentEntity
import ngok3.fyp.backend.operation.student.StudentRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class SocietyServiceTest {
    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()

    private val societyRepository: SocietyRepository = mockk(relaxed = true)
    private val studentRepository: StudentRepository = mockk(relaxed = true)
    private val enrolledSocietyRecordRepository: EnrolledSocietyRecordRepository = mockk()

    private val societyService: SocietyService = SocietyService(
        societyRepository = societyRepository,
        studentRepository = studentRepository,
        enrolledSocietyRecordRepository = enrolledSocietyRecordRepository
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
}