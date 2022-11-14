package ngok3.fyp.backend.society

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

class SocietyService(
    @Autowired val societyRepository: SocietyRepository
) {
    fun getAllSocieties(pageNum: Int, pageSize: Int): List<SocietyDto> {
        val firstPageNumWithPageSizeElement: Pageable = PageRequest.of(pageNum, pageSize)
        val allSocieties = societyRepository.findByOrderByNameAsc(firstPageNumWithPageSizeElement).content

        return allSocieties.map { society ->
            SocietyDto(
                society.uuid,
                society.updatedAt,
                society.createdAt,
                society.name,
            )
        }
    }
}