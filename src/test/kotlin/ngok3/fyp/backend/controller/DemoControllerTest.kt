package ngok3.fyp.backend.controller

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class DemoControllerTest @Autowired constructor(
    val demoController: DemoController
) {
    @Test
    fun demoEndpoint() {
        val result: String = "demo"

        assertEquals(result, demoController.demo())
    }
}