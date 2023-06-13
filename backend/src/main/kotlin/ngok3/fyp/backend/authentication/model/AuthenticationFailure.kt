package ngok3.fyp.backend.authentication.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText

@JsonRootName(namespace = "cas", value = "authenticationFailure")
@JsonIgnoreProperties(ignoreUnknown = true)
data class AuthenticationFailure(
    @set:JacksonXmlProperty(localName = "code", isAttribute = true)
    var code: String? = null,
    @set:JacksonXmlText
    var value: String? = null
)
