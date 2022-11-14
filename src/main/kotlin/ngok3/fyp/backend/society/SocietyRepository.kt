package ngok3.fyp.backend.society

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*

interface SocietyRepository : PagingAndSortingRepository<SocietyEntity, UUID> {

    fun findByOrderByNameAsc(pageable: Pageable): Page<SocietyEntity>

}