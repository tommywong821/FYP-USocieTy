package ngok3.fyp.backend.enrolled_event_record

enum class EnrolledStatus(val status: String) {
    PENDING("pending"), SUCCESS("success"), DECLINE("decline")
}