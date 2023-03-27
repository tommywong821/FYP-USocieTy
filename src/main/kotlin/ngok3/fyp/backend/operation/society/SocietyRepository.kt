package ngok3.fyp.backend.operation.society

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import java.time.LocalDateTime
import java.util.*

interface SocietyRepository : PagingAndSortingRepository<SocietyEntity, UUID> {

    fun findByOrderByNameAsc(pageable: Pageable): Page<SocietyEntity>


    @Query("select s from SocietyEntity s where s.name in ?1")
    fun findByNameIn(names: MutableCollection<String>, pageable: Pageable): List<SocietyEntity>


    @Query("select s from SocietyEntity s where s.name = ?1")
    fun findByName(name: String): Optional<SocietyEntity>


    @Query(
        """select new ngok3.fyp.backend.operation.society.SocietyDto(cast(s.uuid as text), s.name, s.description, count(s)) from SocietyEntity s inner join s.eventRecords eventRecords
where s.name = ?1 and eventRecords.applyDeadline >= ?2 group by s.uuid"""
    )
    fun countBySocietyNameAndApplyDeadline(name: String, applyDeadline: LocalDateTime): SocietyDto

}