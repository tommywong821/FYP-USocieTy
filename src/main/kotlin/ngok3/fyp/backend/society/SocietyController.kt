package ngok3.fyp.backend.society

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("society")
class SocietyController(
    @Autowired val societyService: SocietyService
) {
    @GetMapping
    fun getAllSocieties(
        @RequestParam("pageNum", required = false, defaultValue = "0") pageNum: Int,
        @RequestParam("pageSize", required = false, defaultValue = "10") pageSize: Int
    ): List<SocietyDto> {
        return societyService.getAllSocieties(pageNum, pageSize)
    }

    @PostMapping
    fun joinSociety(
        @RequestParam("itsc", required = false, defaultValue = "") itsc: String,
        @RequestParam("societyId", required = false, defaultValue = "") societyId: String,
    ): Boolean {
        return societyService.joinSociety(itsc, societyId)
    }
}