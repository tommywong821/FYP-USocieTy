package ngok3.fyp.backend.operation.student

import ngok3.fyp.backend.operation.enrolled.EnrolledStatus

data class StudentEnrolledSocietyStatusDto(
    val societyName: String,
    val registerDate: String,
    val enrolledStatus: EnrolledStatus,
)
