package ngok3.fyp.backend.controller.authentication.model

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import ngok3.fyp.backend.authentication.model.ServiceResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class AuthenticationModelTest @Autowired constructor() {
    val xmlMapper = XmlMapper()

    @Test
    fun testAuthenticationModelWithFailure() {
        val authenticationFailureXml = """
            <cas:serviceResponse xmlns:cas="http://www.yale.edu/tp/cas">
              <cas:authenticationFailure code="INVALID_TICKET">E_TICKET_EXPIRED</cas:authenticationFailure>
            </cas:serviceResponse>
        """.trimIndent()

        val failureModelObj: ServiceResponse = xmlMapper.readValue(authenticationFailureXml)
        assertNotNull(failureModelObj.authenticationSuccess)
        assertNull(failureModelObj.authenticationSuccess!!.user)
        assertNull(failureModelObj.authenticationSuccess!!.attributes!!.mail)
        assertNull(failureModelObj.authenticationSuccess!!.attributes!!.name)

        assertNotNull(failureModelObj.authenticationFailure)
        assertEquals(failureModelObj.authenticationFailure!!.code, "INVALID_TICKET")
        assertEquals(failureModelObj.authenticationFailure!!.value, "E_TICKET_EXPIRED")
    }

    @Test
    fun testAuthenticationModelWithSuccess() {
        val authenticationSuccessXml = """
            <?xml version="1.0" encoding="UTF-8"?>
            <cas:serviceResponse xmlns:cas="http://www.yale.edu/tp/cas">
              <cas:authenticationSuccess>
                <cas:user>dmchanxy</cas:user>
                  <cas:attributes>
                        <cas:mail>dmchanxy@connect.ust.hk</cas:mail>
                        <cas:name>CHAN, Dai Man</cas:name>
                  </cas:attributes>
              </cas:authenticationSuccess>
            </cas:serviceResponse>
        """.trimIndent()

        val successModelObj: ServiceResponse = xmlMapper.readValue(authenticationSuccessXml)
        assertNotNull(successModelObj.authenticationFailure)
        assertNull(successModelObj.authenticationFailure!!.code)
        assertNull(successModelObj.authenticationFailure!!.value)

        assertNotNull(successModelObj.authenticationSuccess)
        assertEquals(successModelObj.authenticationSuccess!!.user, "dmchanxy")
        assertNotNull(successModelObj.authenticationSuccess!!.attributes)
        assertEquals(successModelObj.authenticationSuccess!!.attributes!!.mail, "dmchanxy@connect.ust.hk")
        assertEquals(successModelObj.authenticationSuccess!!.attributes!!.name, "CHAN, Dai Man")
    }
}