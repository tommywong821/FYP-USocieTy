package ngok3.fyp.backend.operation.society

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import ngok3.fyp.backend.authentication.role.Role
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.TotalCountDto
import ngok3.fyp.backend.operation.student.StudentDto
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.util.LinkedMultiValueMap
import javax.servlet.http.Cookie

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class SocietyControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {
    @MockkBean
    lateinit var societyService: SocietyService

    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()

    @Test
    fun `should get all student belong to society`() {

        val memberOfSociety: List<StudentDto> = listOf<StudentDto>(
            StudentDto(itsc = "qwerty", nickname = "nickname 1", roles = listOf(Role.ROLE_SOCIETY_MEMBER.toString())),
            StudentDto(itsc = "asdfg", nickname = "nickname 2", roles = listOf()),
        )

        every {
            societyService.getAllSocietyMember(mockAuthRepository.testSocietyName)
        } returns memberOfSociety

        mockMvc.get("/society/member") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("societyName", mockAuthRepository.testSocietyName)
            }
        }.andDo { print() }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$") {
                isArray()
            }
            jsonPath("$.size()") {
                value(memberOfSociety.size)
            }
            jsonPath("$[*].itsc") {
                value(memberOfSociety.map { studentDto -> studentDto.itsc })
            }
            jsonPath("$[*].nickname") {
                value(memberOfSociety.map { studentDto -> studentDto.nickname })
            }
            jsonPath("$[*].roles") {
                value(memberOfSociety.map { studentDto -> studentDto.roles })
            }
        }
    }

    @Test
    fun `should assign society member role to student`() {

        val studentIdList: List<String> = listOf<String>(
            "2ac23d21-4cb0-4173-a2fe-de551ec5aa9d",
            "38153605-ed2c-42e7-947a-9d1731f4bd44"
        )

        every {
            societyService.assignSocietyMemberRole(mockAuthRepository.testSocietyName, studentIdList)
        } returns Unit

        mockMvc.post("/society/member") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
            }
            content =
                "{\"societyName\":\"test society\",\"studentIdList\":[\"2ac23d21-4cb0-4173-a2fe-de551ec5aa9d\",\"38153605-ed2c-42e7-947a-9d1731f4bd44\"]}"
        }.andDo { print() }.andExpect {
            status { isCreated() }
        }
    }

    @Test
    fun `should remove society member role from student`() {

        val studentIdList: List<String> = listOf<String>(
            "2ac23d21-4cb0-4173-a2fe-de551ec5aa9d",
            "38153605-ed2c-42e7-947a-9d1731f4bd44"
        )

        every {
            societyService.removeSocietyMemberRole(mockAuthRepository.testSocietyName, studentIdList)
        } returns Unit

        mockMvc.delete("/society/member") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("societyName", mockAuthRepository.testSocietyName)
                add("id", "2ac23d21-4cb0-4173-a2fe-de551ec5aa9d")
                add("id", "38153605-ed2c-42e7-947a-9d1731f4bd44")
            }
        }.andDo { print() }.andExpect {
            status { isAccepted() }
        }
    }

    @Test
    fun `should number of event held by society`() {
        val totalNumber: TotalCountDto = TotalCountDto(123)

        every {
            societyService.getTotalNumberOfHoldingEvent(mockAuthRepository.testSocietyName)
        } returns totalNumber

        mockMvc.get("/society/holdingEvent") {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
            }
            params = LinkedMultiValueMap<String, String>().apply {
                add("societyName", mockAuthRepository.testSocietyName)
            }
        }.andDo { print() }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.totalNumber") {
                value(123)
            }
        }
    }
}