package ngok3.fyp.backend.operation.enrolled.society_record

import ngok3.fyp.backend.operation.enrolled.EnrolledStatus

data class StudentEnrolledEventRecord(
    val itsc: String,
    val nickname: String,
    val status: EnrolledStatus
)
