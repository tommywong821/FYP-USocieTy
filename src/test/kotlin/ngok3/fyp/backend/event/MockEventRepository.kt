package ngok3.fyp.backend.event

import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

class MockEventRepository {


    val allTestEventList: List<EventEntity> = listOf(
        EventEntity("test event 1", "test poster 1", 10, null, "test location 1", null, null),
        EventEntity("test event 2", "test poster 2", 20, null, "test location 2", null, null),
        EventEntity("test event 3", "test poster 3", 30, null, "test location 3", null, null),
        EventEntity("test event 4", "test poster 4", 40, null, "test location 4", null, null),
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