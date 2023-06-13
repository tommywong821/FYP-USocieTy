package ngok3.fyp.backend.operation.enrolled.society_record.model

import ngok3.fyp.backend.operation.enrolled.EnrolledStatus

data class UpdateEnrolledSocietyRecordDto(
    val societyId: String,
    val studentId: String,
    val status: EnrolledStatus,
)
