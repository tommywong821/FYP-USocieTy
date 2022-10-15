package ngok3.fyp.backend.event

import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface EventRepository : PagingAndSortingRepository<EventEntity, Long> {
}