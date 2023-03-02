package ngok3.fyp.backend.operation.event.dto

import ngok3.fyp.backend.operation.event.EventEntity
import ngok3.fyp.backend.util.DateUtil
import java.io.Serializable

/**
 * A DTO for the {@link ngok3.fyp.backend.event.EventEntity} entity
 */
data class EventDto(
    val id: String = "",
    val name: String = "",
    var poster: String = "",
    val maxParticipation: Int = -1,
    val applyDeadline: String = "",
    val location: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val category: String = "",
    val description: String = "",
    val fee: Double = 0.0,
    val version: Long = 0
) : Serializable {

    private var dateUtil: DateUtil = DateUtil()

    fun createFromEntity(eventEntity: EventEntity) = EventDto(
        id = eventEntity.uuid.toString(),
        name = eventEntity.name,
        maxParticipation = eventEntity.maxParticipation,
        applyDeadline = dateUtil.convertLocalDateTimeToString(eventEntity.applyDeadline),
        location = eventEntity.location,
        startDate = dateUtil.convertLocalDateTimeToString(eventEntity.startDate),
        endDate = dateUtil.convertLocalDateTimeToString(eventEntity.endDate),
        category = eventEntity.category,
        description = eventEntity.description,
        fee = eventEntity.fee,
        poster = eventEntity.poster,
        version = eventEntity.version
    )

    fun toEntity() = EventEntity(
        name = this.name,
        maxParticipation = this.maxParticipation,
        applyDeadline = dateUtil.convertStringWithTimeStampToLocalDateTime(this.applyDeadline),
        location = this.location,
        startDate = dateUtil.convertStringWithTimeStampToLocalDateTime(this.startDate),
        endDate = dateUtil.convertStringWithTimeStampToLocalDateTime(this.endDate),
        category = this.category,
        description = this.description,
        fee = this.fee,
        poster = this.poster,
        version = this.version + 1
    )
}