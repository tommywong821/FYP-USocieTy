package ngok3.fyp.backend.operation.society

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import java.util.*

interface SocietyRepository : PagingAndSortingRepository<SocietyEntity, UUID> {

    fun findByOrderByNameAsc(pageable: Pageable): Page<SocietyEntity>


    @Query("select s from SocietyEntity s where s.name = ?1")
    fun findByName(name: String): Optional<SocietyEntity>
}