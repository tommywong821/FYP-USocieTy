package ngok3.fyp.backend.operation.s3


data class S3BulkResponseEntity constructor(
    var bucket: String,
    var fileKey: String,
    var originFileName: String,
    var successful: Boolean,
    var statusCode: Int
)
