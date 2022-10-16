package ngok3.fyp.backend.student

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@SpringBootTest
@AutoConfigureMockMvc
class StudentControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {
    @MockkBean
    lateinit var studentService: StudentService
    private val studentDto: StudentDto = StudentDto("test_itsc", "test_name", "test_mail", "test_role")

    @Test
    fun testGetStudentProfile() {
        every { studentService.getStudentProfile("test_itsc") } returns studentDto

        mockMvc.perform(
            MockMvcRequestBuilders.get("/student")
                .param("itsc", "test_itsc")
        ).andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$.itsc").value(studentDto.itsc))
            .andExpect(MockMvcResultMatchers.jsonPath("$.nickname").value(studentDto.nickname))
            .andExpect(MockMvcResultMatchers.jsonPath("$.mail").value(studentDto.mail))
            .andExpect(MockMvcResultMatchers.jsonPath("$.role").value(studentDto.role))
    }
}