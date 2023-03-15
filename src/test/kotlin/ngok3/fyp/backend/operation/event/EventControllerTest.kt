package ngok3.fyp.backend.operation.event

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import ngok3.fyp.backend.controller.authentication.model.MockAuthRepository
import ngok3.fyp.backend.operation.event.dto.EventDto
import ngok3.fyp.backend.operation.student.MockStudentRepository
import ngok3.fyp.backend.util.DateUtil
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*
import javax.servlet.http.Cookie

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class EventControllerTest @Autowired constructor(
    val mockMvc: MockMvc
) {
    private val mockEventRepository: MockEventRepository = MockEventRepository()
    private val mockStudentRepository: MockStudentRepository = MockStudentRepository()
    private val mockAuthRepository: MockAuthRepository = MockAuthRepository()

    private val dateUtil: DateUtil = DateUtil()

    @MockkBean
    lateinit var eventService: EventService

    @Test
    fun `should return all society event`() {
        every {
            eventService.getAllEvent(
                mockEventRepository.testPageNumWithoutSid,
                mockEventRepository.testPageSizeWithoutSid
            )
        } returns mockEventRepository.allTestEventList.map { eventEntity ->
            EventDto().createFromEntity(eventEntity, "s3Bucket")
        }

        val allEventList = mockEventRepository.allTestEventList

        mockMvc.get("/event?pageNum=${mockEventRepository.testPageNumWithoutSid}&pageSize=${mockEventRepository.testPageSizeWithoutSid}") {
            cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
        }
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
                    value(allEventList.map { eventEntity -> "s3Bucket${eventEntity.poster}" })
                }
                jsonPath("$[*].maxParticipation") {
                    value(allEventList.map { eventEntity -> eventEntity.maxParticipation })
                }
                jsonPath("$[*].applyDeadline") {
                    value(allEventList.map { eventEntity ->
                        dateUtil.convertLocalDateTimeToString(eventEntity.applyDeadline)
                    })
                }
                jsonPath("$[*].location") {
                    value(allEventList.map { eventEntity -> eventEntity.location })
                }
            }
    }

    @Test
    fun `should return society event of student with sid`() {
        every {
            eventService.getAllEvent(
                mockEventRepository.testPageNumWithSid,
                mockEventRepository.testPageSizeWithSid
            )
        } returns mockEventRepository.allTestEventList.subList(
            mockEventRepository.testFromIndex,
            mockEventRepository.testToIndex
        ).map { eventEntity ->
            EventDto().createFromEntity(eventEntity, "s3Bucket")
        }

        val allEventList = mockEventRepository.allTestEventList.subList(
            mockEventRepository.testFromIndex,
            mockEventRepository.testToIndex
        )

        mockMvc.get("/event?itsc=${mockStudentRepository.testItsc}&pageNum=${mockEventRepository.testPageNumWithSid}&pageSize=${mockEventRepository.testPageSizeWithSid}") {
            cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
        }
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
                    value(allEventList.map { eventEntity -> "s3Bucket${eventEntity.poster}" })
                }
                jsonPath("$[*].maxParticipation") {
                    value(allEventList.map { eventEntity -> eventEntity.maxParticipation })
                }
                jsonPath("$[*].applyDeadline") {
                    value(allEventList.map { eventEntity ->
                        dateUtil.convertLocalDateTimeToString(eventEntity.applyDeadline)
                    })
                }
                jsonPath("$[*].location") {
                    value(allEventList.map { eventEntity -> eventEntity.location })
                }
            }
    }

    @Test
    fun `should delete event with event id`() {
        val uuid: String = UUID.randomUUID().toString()

        every { eventService.deleteEvent(mockAuthRepository.validUserCookieToken, uuid) } returns Unit

        mockMvc.delete("/event/{eventId}", uuid) {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
            }
            pathInfo
        }.andDo { print() }.andExpect {
            status { isNoContent() }
        }
    }

    @Test
    fun `should update event with event id`() {
        val uuid: String = UUID.randomUUID().toString()
        val updateEventDto = EventDto(
            name = "update name",
            maxParticipation = 10,
            applyDeadline = "2022-01-12T12:10:10.222Z",
            location = "update location",
            startDate = "2022-01-10T12:10:10.222Z",
            endDate = "2022-01-15T12:10:10.222Z",
            category = EventCategory.OUTDOOR,
            description = "update description",
            fee = 12.3
        )

        every {
            eventService.updateEvent(
                mockAuthRepository.validUserCookieToken,
                uuid,
                updateEventDto,
                any()
            )
        } returns Unit

        mockMvc.perform(
            MockMvcRequestBuilders.multipart(
                HttpMethod.PUT,
                "/event/$uuid"
            )
                .file(MockMultipartFile("poster", "poster".encodeToByteArray()))
                .file(
                    MockMultipartFile(
                        "event",
                        "test.json",
                        MediaType.APPLICATION_JSON.toString(),
                        "{\"name\":\"update name\",\"maxParticipation\":10,\"applyDeadline\":\"2022-01-12T12:10:10.222Z\",\"location\":\"update location\",\"startDate\":\"2022-01-10T12:10:10.222Z\",\"endDate\":\"2022-01-15T12:10:10.222Z\",\"category\":\"OUTDOOR\",\"description\":\"update description\",\"fee\":12.3}".encodeToByteArray()
                    )
                )
                .cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        ).andExpect(status().isNoContent)
    }

    @Test
    fun `should get event with event id`() {
        val uuid: String = UUID.randomUUID().toString()
        val eventDto = EventDto(
            name = "update name",
            maxParticipation = 10,
            applyDeadline = "2022-01-12T12:10:10.222Z",
            location = "update location",
            startDate = "2022-01-10T12:10:10.222Z",
            endDate = "2022-01-15T12:10:10.222Z",
            category = EventCategory.OUTDOOR,
            description = "update description",
            fee = 12.3,
            society = "test society"
        )

        every { eventService.getEventWithUuid(mockAuthRepository.validUserCookieToken, uuid) } returns eventDto

        mockMvc.get("/event/{eventId}", uuid) {
            headers {
                contentType = MediaType.APPLICATION_JSON
                cookie(Cookie("token", mockAuthRepository.validUserCookieToken))
            }
            pathInfo
        }.andDo { print() }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.name") {
                value(eventDto.name)
            }
            jsonPath("$.maxParticipation") {
                value(eventDto.maxParticipation)
            }
            jsonPath("$.applyDeadline") {
                value(eventDto.applyDeadline)
            }
            jsonPath("$.maxParticipation") {
                value(eventDto.maxParticipation)
            }
            jsonPath("$.location") {
                value(eventDto.location)
            }
            jsonPath("$.startDate") {
                value(eventDto.startDate)
            }
            jsonPath("$.endDate") {
                value(eventDto.endDate)
            }
            jsonPath("$.category") {
                value(eventDto.category.toString())
            }
            jsonPath("$.description") {
                value(eventDto.description)
            }
            jsonPath("$.fee") {
                value(eventDto.fee)
            }
            jsonPath("$.society") {
                value(eventDto.society)
            }
        }
    }
}