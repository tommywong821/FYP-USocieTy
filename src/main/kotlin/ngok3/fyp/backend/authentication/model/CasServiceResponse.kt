package ngok3.fyp.backend.authentication.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty


//HKUST ITSC SSO Response
@JsonRootName(namespace = "cas", value = "serviceResponse")
@JsonIgnoreProperties(ignoreUnknown = true)
data class CasServiceResponse(
    @set:JacksonXmlProperty(namespace = "cas", localName = "authenticationFailure")
    var authenticationFailure: AuthenticationFailure? = AuthenticationFailure(),

    @set:JacksonXmlProperty(namespace = "cas", localName = "authenticationSuccess")
    var authenticationSuccess: AuthenticationSuccess? = AuthenticationSuccess()
)
