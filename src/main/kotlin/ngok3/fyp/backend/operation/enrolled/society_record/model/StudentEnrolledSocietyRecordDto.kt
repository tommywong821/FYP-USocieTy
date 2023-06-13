package ngok3.fyp.backend.operation.enrolled.society_record.model

import ngok3.fyp.backend.operation.enrolled.EnrolledStatus

data class StudentEnrolledSocietyRecordDto(
    val studentId: String,
    val societyId: String,
    val itsc: String,
    val name: String,
    val status: EnrolledStatus
)
