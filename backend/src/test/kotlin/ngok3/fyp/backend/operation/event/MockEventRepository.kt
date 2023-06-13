package ngok3.fyp.backend.operation.event

import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import java.time.LocalDateTime
import java.time.ZoneId

class MockEventRepository {


    val allTestEventList: List<EventEntity> = listOf(
        EventEntity(
            "test event 1",
            "test poster 1",
            10,
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")),
            "test location 1",
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")),
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
        ),
        EventEntity(
            "test event 2",
            "test poster 2",
            20,
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")),
            "test location 2",
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")),
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
        ),
        EventEntity(
            "test event 3",
            "test poster 3",
            30,
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")),
            "test location 3",
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")),
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
        ),
        EventEntity(
            "test event 4",
            "test poster 4",
            40,
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")),
            "test location 4",
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong")),
            LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
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