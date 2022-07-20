package ngok3.fyp.backend.authentication.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

@JsonRootName(namespace = "cas", value = "authenticationSuccess")
@JsonIgnoreProperties(ignoreUnknown = true)
data class AuthenticationSuccess(
    @set:JacksonXmlProperty(localName = "user")
    var user: String? = null,

    @set:JacksonXmlProperty(namespace = "cas", localName = "attributes")
    var attributes: CasAttributes? = CasAttributes()
)
