package ngok3.fyp.backend.operation.student.model

import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import java.io.Serializable

data class StudentEnrolledSocietyStatusDto(
    val societyName: String,
    val registerDate: String,
    val enrolledStatus: EnrolledStatus,
) : Serializable
