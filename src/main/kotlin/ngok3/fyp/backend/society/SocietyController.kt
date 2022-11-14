package ngok3.fyp.backend.society

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.RequestParam

class SocietyController(
    @Autowired val societyService: SocietyService
) {
    fun getAllSocieties(
        @RequestParam("pageNum", required = false, defaultValue = "0") pageNum: Int,
        @RequestParam("pageSize", required = false, defaultValue = "10") pageSize: Int
    ): List<SocietyDto> {
        return societyService.getAllSocieties(pageNum, pageSize)
    }
}