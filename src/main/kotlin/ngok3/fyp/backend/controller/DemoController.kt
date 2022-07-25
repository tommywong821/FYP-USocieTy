package ngok3.fyp.backend.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/demo")
class DemoController {
    @GetMapping
    fun demo(): Demo {
        println("hello")
        return Demo("frontendUrl")
    }

}