package ngok3.fyp.backend.operation.event

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*

@Repository
interface EventRepository : PagingAndSortingRepository<EventEntity, UUID> {

    fun findByName(name: String): Optional<EventEntity>


    @Query("select e from EventEntity e where e.applyDeadline >= ?1 order by e.applyDeadline")
    fun findByApplyDeadlineGreaterThanEqualOrderByApplyDeadlineAsc(
        applyDeadline: LocalDateTime,
        pageable: Pageable
    ): Page<EventEntity>


    @Query("select e from EventEntity e where e.societyEntity.name = ?1")
    fun findAllBySocietyName(name: String, pageable: Pageable): List<EventEntity>


    @Query("select e from EventEntity e where e.societyEntity.name in ?1 order by e.applyDeadline desc")
    fun findAllBySocietyNameListOrderByApplyDeadlineDesc(
        names: MutableCollection<String>,
        pageable: Pageable
    ): List<EventEntity>


    @Query("select count(e) from EventEntity e where e.societyEntity.name in ?1")
    fun countBySocietyNameList(names: MutableCollection<String>): Long
}