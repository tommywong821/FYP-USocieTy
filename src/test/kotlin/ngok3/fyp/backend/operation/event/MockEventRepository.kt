package ngok3.fyp.backend.operation.event

import ngok3.fyp.backend.operation.event.EventEntity
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime

class MockEventRepository {


    val allTestEventList: List<EventEntity> = listOf(
        EventEntity(
            "test event 1",
            "test poster 1",
            10,
            LocalDateTime.now(),
            "test location 1",
            LocalDateTime.now(),
            LocalDateTime.now()
        ),
        EventEntity(
            "test event 2",
            "test poster 2",
            20,
            LocalDateTime.now(),
            "test location 2",
            LocalDateTime.now(),
            LocalDateTime.now()
        ),
        EventEntity(
            "test event 3",
            "test poster 3",
            30,
            LocalDateTime.now(),
            "test location 3",
            LocalDateTime.now(),
            LocalDateTime.now()
        ),
        EventEntity(
            "test event 4",
            "test poster 4",
            40,
            LocalDateTime.now(),
            "test location 4",
            LocalDateTime.now(),
            LocalDateTime.now()
        ),
    )

    val testPageNumWithoutSid = 0
    val testPageSizeWithoutSid = 2
    val testPageableWithoutSid = PageRequest.of(testPageNumWithoutSid, testPageSizeWithoutSid)
    val withoutSidTestEventPage = PageImpl<EventEntity>(
        allTestEventList.subList(testPageNumWithoutSid, testPageSizeWithoutSid),
        testPageableWithoutSid,
        allTestEventList.subList(testPageNumWithoutSid, testPageSizeWithoutSid).size.toLong()
    )

    val testPageNumWithSid = 1
    val testPageSizeWithSid = 2
    val testFromIndex = testPageNumWithSid * testPageSizeWithSid
    val testToIndex = testPageNumWithSid * testPageSizeWithSid + testPageSizeWithSid
    val testPageableWithSid = PageRequest.of(testPageNumWithSid, testPageSizeWithSid)
    val withSidTestEventPage = PageImpl<EventEntity>(
        allTestEventList.subList(testFromIndex, testToIndex),
        testPageableWithSid,
        allTestEventList.subList(testFromIndex, testToIndex).size.toLong()
    )

}