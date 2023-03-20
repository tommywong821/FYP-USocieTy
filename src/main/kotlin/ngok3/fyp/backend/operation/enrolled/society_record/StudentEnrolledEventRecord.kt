package ngok3.fyp.backend.operation.enrolled.society_record

import ngok3.fyp.backend.operation.enrolled.EnrolledStatus

data class StudentEnrolledEventRecord(
    val studentId: String,
    val societyId: String,
    val status: EnrolledStatus
)
