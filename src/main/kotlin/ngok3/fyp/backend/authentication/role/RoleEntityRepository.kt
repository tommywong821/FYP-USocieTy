package ngok3.fyp.backend.authentication.role;

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface RoleEntityRepository : JpaRepository<RoleEntity, Int> {


    @Query("select r from RoleEntity r where r.role = ?1")
    fun findByRole(role: Role): RoleEntity

}