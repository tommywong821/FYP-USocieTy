package ngok3.fyp.backend.authentication.student_role

import ngok3.fyp.backend.authentication.role.Role
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface StudentRoleEntityRepository : CrudRepository<StudentRoleEntity, UUID> {


    @Query(
        """select s from StudentRoleEntity s
where s.studentEntity.itsc = ?1 and s.societyEntity.name = ?2 and s.roleEntity.role = ?3"""
    )
    fun findByStudentItscAndSocietyNameAndRole(
        itsc: String,
        name: String,
        role: Role
    ): Optional<StudentRoleEntity>


    @Query(
        """select s from StudentRoleEntity s inner join s.societyEntity.eventRecords eventRecords
where s.studentEntity.itsc = ?1 and s.roleEntity.role = ?2 and eventRecords.uuid = ?3"""
    )
    fun findByUserItscAndUserRoleAndHisSocietyIsHoldingEvent(
        itsc: String,
        role: Role,
        eventUuid: UUID
    ): Optional<StudentRoleEntity>


    @Query("select s from StudentRoleEntity s where s.studentEntity.uuid in ?1 and s.societyEntity.name = ?2")
    fun findByStudentEntity_UuidInAndSocietyEntity_Name(
        uuids: MutableCollection<UUID>,
        name: String
    ): List<StudentRoleEntity>

}