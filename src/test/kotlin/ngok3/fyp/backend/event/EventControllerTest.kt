package ngok3.fyp.backend.event

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import ngok3.fyp.backend.student.MockStudentRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get

@SpringBootTest
@AutoConfigureMockMvc
class EventControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {
    private val mockEventRepository: MockEventRepository = MockEventRepository()
    private val mockStudentRepository: MockStudentRepository = MockStudentRepository()

    @MockkBean
    lateinit var eventService: EventService

    @Test
    fun `should return all society event`() {
        every {
            eventService.getAllSocietyEvent(
                "",
                mockEventRepository.testPageNumWithoutSid,
                mockEventRepository.testPageSizeWithoutSid
            )
        } returns mockEventRepository.allTestEventList.map { eventEntity ->
            EventDto(eventEntity)
        }

        val allEventList = mockEventRepository.allTestEventList

        mockMvc.get("/event?pageNum=${mockEventRepository.testPageNumWithoutSid}&pageSize=${mockEventRepository.testPageSizeWithoutSid}")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$") {
                    isArray()
                }
                jsonPath("$.size()") {
                    value(allEventList.size)
                }
                jsonPath("$[*].name") {
                    value(allEventList.map { eventEntity -> eventEntity.name })
                }
                jsonPath("$[*].poster") {
                    value(allEventList.map { eventEntity -> eventEntity.poster })
                }
                jsonPath("$[*].maxParticipation") {
                    value(allEventList.map { eventEntity -> eventEntity.maxParticipation })
                }
                jsonPath("$[*].applyDeadline") {
                    value(allEventList.map { eventEntity -> eventEntity.applyDeadline })
                }
                jsonPath("$[*].location") {
                    value(allEventList.map { eventEntity -> eventEntity.location })
                }
            }
    }

    @Test
    fun `should return society event of student with sid`() {
        every {
            eventService.getAllSocietyEvent(
                mockStudentRepository.testItsc,
                mockEventRepository.testPageNumWithSid,
                mockEventRepository.testPageSizeWithSid
            )
        } returns mockEventRepository.allTestEventList.subList(
            mockEventRepository.testFromIndex,
            mockEventRepository.testToIndex
        ).map { eventEntity ->
            EventDto(eventEntity)
        }

        val allEventList = mockEventRepository.allTestEventList.subList(
            mockEventRepository.testFromIndex,
            mockEventRepository.testToIndex
        )

        mockMvc.get("/event?itsc=${mockStudentRepository.testItsc}&pageNum=${mockEventRepository.testPageNumWithSid}&pageSize=${mockEventRepository.testPageSizeWithSid}")
            .andDo { print() }
            .andExpect {
                status { isOk() }
                content { contentType(MediaType.APPLICATION_JSON) }
                jsonPath("$") {
                    isArray()
                }
                jsonPath("$.size()") {
                    value(allEventList.size)
                }
                jsonPath("$[*].name") {
                    value(allEventList.map { eventEntity -> eventEntity.name })
                }
                jsonPath("$[*].poster") {
                    value(allEventList.map { eventEntity -> eventEntity.poster })
                }
                jsonPath("$[*].maxParticipation") {
                    value(allEventList.map { eventEntity -> eventEntity.maxParticipation })
                }
                jsonPath("$[*].applyDeadline") {
                    value(allEventList.map { eventEntity -> eventEntity.applyDeadline })
                }
                jsonPath("$[*].location") {
                    value(allEventList.map { eventEntity -> eventEntity.location })
                }
            }
    }
}