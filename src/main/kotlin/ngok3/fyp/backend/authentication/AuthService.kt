package ngok3.fyp.backend.authentication

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ngok3.fyp.backend.authentication.model.ServiceResponse
import ngok3.fyp.backend.webclient.OkHttpClientFactory
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val webClient: OkHttpClient = OkHttpClientFactory().webClient
) {
    @Value("\${heroku.frontend.url}")
    val frontendUrl: String? = null

    fun itscSSOServiceValidate(ticket: String): ServiceResponse {
        val url: HttpUrl = HttpUrl.Builder()
            .scheme("https")
            .host("cas.ust.hk")
            .addPathSegment("cas")
            .addPathSegment("p3")
            .addPathSegment("serviceValidate")
            .addQueryParameter("service", frontendUrl)
            .addQueryParameter("ticket", ticket)
            .build()
        val request: Request = Request.Builder().url(url).build()
        val response: Response = webClient.newCall(request).execute()
        val xmlMapper = XmlMapper();
        return xmlMapper.readValue(response.body!!.string())
    }
}

