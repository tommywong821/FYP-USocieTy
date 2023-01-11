package ngok3.fyp.backend.operation.student

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StudentRepository : CrudRepository<StudentEntity, UUID> {
    fun findByItsc(itsc: String): Optional<StudentEntity>
}