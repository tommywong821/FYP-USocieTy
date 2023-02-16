package ngok3.fyp.backend.operation.event.dto

import ngok3.fyp.backend.operation.event.EventEntity
import ngok3.fyp.backend.util.DateUtil
import java.io.Serializable
import java.time.format.DateTimeFormatter

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
) : Serializable {

    private var dateUtil: DateUtil = DateUtil()

    constructor(eventEntity: EventEntity) : this(
        eventEntity.uuid.toString(),
        eventEntity.name,
        eventEntity.poster,
        eventEntity.maxParticipation,
        eventEntity.applyDeadline.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        ),
        eventEntity.location,
        eventEntity.startDate.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        ),
        eventEntity.endDate.format(
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        ),
        eventEntity.category,
        eventEntity.description,
        eventEntity.fee
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
        fee = this.fee
    )
}