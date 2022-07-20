package ngok3.fyp.backend.authentication

import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import okhttp3.internal.concurrent.TaskRunner.Companion.logger
import okio.Buffer
import org.springframework.stereotype.Service
import java.io.IOException

@Service
class AuthService(
    private val webClient: OkHttpClient = OkHttpClient.Builder().addInterceptor(LoggingInterceptor()).build()
) {
    fun itscSSOServiceValidate(ticket: String): Any {
        val url: HttpUrl = HttpUrl.Builder()
            .scheme("https")
            .host("cas.ust.hk")
            .addPathSegment("cas")
            .addPathSegment("p3")
            .addPathSegment("serviceValidate")
            .addQueryParameter("service", "https://ngok3fyp-frontend.herokuapp.com/")
            .addQueryParameter("ticket", ticket)
            .build()
        val request: Request = Request.Builder().url(url).build()
        val response: Response = webClient.newCall(request).execute()
        return response.body!!.string()
    }
}

internal class LoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        logger.info("Sending request: Method: ${request.method} Url: ${request.url} headers: ${request.headers}")
        val requestCopy = request.newBuilder().build()
        val bodyBuffer = Buffer()
        requestCopy.body?.writeTo(bodyBuffer)
        logger.info("Request Body: ${bodyBuffer.readUtf8()}")
        bodyBuffer.clear()

        val response: Response = chain.proceed(request)
        val contentType = response.body!!.contentType()
        val body = response.body!!.string()
        logger.info("Response Body: $body")

        val wrappedBody = body.toResponseBody(contentType)
        return response.newBuilder().body(wrappedBody).build()
    }
}
