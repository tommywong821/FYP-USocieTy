package ngok3.fyp.backend.operation.s3

import io.swagger.v3.oas.annotations.Hidden
import ngok3.fyp.backend.operation.event.dto.EventDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

//hidden in swagger for development only
@Hidden
@RestController
@RequestMapping("/s3")
class S3Controller(
    val s3Service: S3Service
) {
    @GetMapping("{bucket}")
    fun getFileList(
        @PathVariable("bucket") bucket: String
    ): ResponseEntity<List<String>> {
        return ResponseEntity.of(Optional.of(s3Service.getBucketFileList(bucket)))
    }

    @PostMapping("{bucket}")
    fun uploadFile(
        @PathVariable("bucket") bucket: String,
        @RequestPart("poster") uploadFiles: Array<MultipartFile>,
        @RequestPart("event") event: EventDto,
    ): ResponseEntity<Any?> {
        print(event)
        val awsResponse = s3Service.uploadFiles("society/event", uploadFiles)
        return ResponseEntity(awsResponse, HttpStatus.OK)
    }
}