package ngok3.fyp.backend.authentication.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

@JsonRootName(namespace = "cas", value = "attributes")
@JsonIgnoreProperties(ignoreUnknown = true)
data class CasAttributes(
    @set:JacksonXmlProperty(localName = "mail")
    var mail: String? = null,

    @set:JacksonXmlProperty(localName = "name")
    var name: String? = null,
)
