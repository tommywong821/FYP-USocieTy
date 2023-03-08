package ngok3.fyp.backend.operation.s3

import org.apache.commons.io.FilenameUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import java.util.*

@Service
class S3Service(
    val s3Client: S3Client,
) {
    @Value("\${aws.bucket.name}")
    lateinit var bucketName: String
    fun getBucketFileList(bucketName: String): List<String> {
        return s3Client
            .listObjectsV2(ListObjectsV2Request.builder().bucket(bucketName).build()).contents()
            .map { s3Object -> s3Object.key() }
    }

    fun uploadFiles(bucketFolderName: String, files: Array<MultipartFile>): List<S3BulkResponseEntity> {
        return this.uploadFiles(bucketFolderName, files, 0)
    }

    fun uploadFiles(bucketFolderName: String, files: Array<MultipartFile>, version: Long): List<S3BulkResponseEntity> {
        val responses: ArrayList<S3BulkResponseEntity> = ArrayList()

//        check if bucket exist
        try {
            s3Client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build()).sdkHttpResponse()

        } catch (e: NoSuchBucketException) {
            s3Client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build()).sdkHttpResponse()

        } catch (e: Exception) {
            throw Exception("Accessing no permission bucket: $bucketName")
        }

        files.forEach { file ->
            run {
                val originFileName: String? = file.originalFilename
                val uuid: String = UUID.randomUUID().toString()
                val fileName: String = "${bucketFolderName}${uuid}_${version}"
                val fileExtension: String = FilenameUtils.getExtension(file.originalFilename)
                responses.add(
                    s3Client.putObject(
                        PutObjectRequest.builder().bucket(bucketName).key("$fileName.${fileExtension}")
                            .build(),
                        RequestBody.fromBytes(file.bytes)
                    )
                        .sdkHttpResponse()
                        .also { x -> print("AWS S3 uploadFile \"$originFileName\" as \"$uuid\" to \"$bucketFolderName\" code ${x.statusCode()}") }
                        .let { response ->
                            S3BulkResponseEntity(
                                bucket = bucketFolderName,
                                fileKey = "$fileName.$fileExtension",
                                originFileName = originFileName ?: "no name",
                                successful = response.isSuccessful,
                                statusCode = response.statusCode()
                            )
                        })
            }
        }
        return responses
    }

}