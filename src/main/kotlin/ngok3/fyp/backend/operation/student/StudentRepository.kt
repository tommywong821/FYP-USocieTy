package ngok3.fyp.backend.operation.student

import ngok3.fyp.backend.authentication.role.Role
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StudentRepository : CrudRepository<StudentEntity, UUID> {
    fun findByItsc(itsc: String): Optional<StudentEntity>


    @Query("select s from StudentEntity s inner join s.roles roles where s.itsc = ?1 and roles.role = ?2")
    fun findByItscAndRoles_Role(itsc: String, role: Role): Optional<StudentEntity>


    @Query(
        """select s from StudentEntity s inner join s.enrolledSocietyRecordEntity enrolledSocietyRecordEntity
where enrolledSocietyRecordEntity.societyEntity.name = ?1"""
    )
    fun findByEnrolledSocietyName(name: String): List<StudentEntity>

}