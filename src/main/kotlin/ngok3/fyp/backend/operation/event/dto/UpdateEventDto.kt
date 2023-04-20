package ngok3.fyp.backend.operation.event.dto

import ngok3.fyp.backend.operation.event.EventEntity
import ngok3.fyp.backend.util.DateUtil
import java.io.Serializable

data class UpdateEventDto(
    val id: String = "",
    val name: String = "",
    var poster: String = "",
    val maxParticipation: Int = -1,
    val applyDeadline: String = "",
    val location: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val category: EventCategory = EventCategory.OUTDOOR,
    val description: String = "",
    val fee: Double = 0.0,
    val version: Long = 0,
    val society: String? = "",
    var societyHoldingEventNumber: Long? = 0
) : Serializable {

    private var dateUtil: DateUtil = DateUtil()

    fun createFromEntity(eventEntity: EventEntity, s3BucketDomain: String) = UpdateEventDto(
        id = eventEntity.uuid.toString(),
        name = eventEntity.name,
        maxParticipation = eventEntity.maxParticipation,
        applyDeadline = dateUtil.convertLocalDateTimeToStringWithTimeStampWithoutOffset(eventEntity.applyDeadline),
        location = eventEntity.location,
        startDate = dateUtil.convertLocalDateTimeToStringWithTimeStampWithoutOffset(eventEntity.startDate),
        endDate = dateUtil.convertLocalDateTimeToStringWithTimeStampWithoutOffset(eventEntity.endDate),
        category = eventEntity.category,
        description = eventEntity.description,
        fee = eventEntity.fee,
        poster = "${s3BucketDomain}${eventEntity.poster}",
        version = eventEntity.version,
        society = eventEntity.societyEntity.name
    )
}