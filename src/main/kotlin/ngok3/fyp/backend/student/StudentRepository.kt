package ngok3.fyp.backend.student

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StudentRepository : CrudRepository<Student, Long> {
    fun findByItsc(itsc: String): Optional<Student>
}