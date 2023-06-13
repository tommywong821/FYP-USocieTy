package ngok3.fyp.backend.util.webclient

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.concurrent.TaskRunner
import okio.Buffer
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class OkHttpClientFactory(
    val webClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(LoggingInterceptor()).build()
)

//Log every request, response info
internal class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        //logging request
        TaskRunner.logger.info("Sending request: Method: ${request.method} Url: ${request.url} headers: ${request.headers}")
        val requestCopy = request.newBuilder().build()
        val bodyBuffer = Buffer()
        requestCopy.body?.writeTo(bodyBuffer)
        TaskRunner.logger.info("Request Body: ${bodyBuffer.readUtf8()}")
        bodyBuffer.clear()

        //logging response
        val response: Response = chain.proceed(request)
        val contentType = response.body!!.contentType()
        //response body can only stream once
        val body = response.body!!.string()
        TaskRunner.logger.info("Response Body: $body")

        //build new response with body
        val wrappedBody = body.toResponseBody(contentType)
        return response.newBuilder().body(wrappedBody).build()
    }
}