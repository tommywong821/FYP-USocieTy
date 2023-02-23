package ngok3.fyp.backend.operation.enrolled

enum class EnrolledStatus(val status: String) {
    PENDING("pending"), SUCCESS("success"), DECLINE("decline")
}