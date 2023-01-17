package ngok3.fyp.backend.operation.event.dto

data class CreateEventDto(
    val eventDto: EventDto,
    val itsc: String,
    val society: String,
)
