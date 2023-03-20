package ngok3.fyp.backend.operation.student

import ngok3.fyp.backend.authentication.role.Role
import ngok3.fyp.backend.operation.enrolled.EnrolledStatus
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface StudentRepository : CrudRepository<StudentEntity, UUID> {
    fun findByItsc(itsc: String): Optional<StudentEntity>


    @Query("select s from StudentEntity s inner join s.studentRoleEntities studentRoles where s.itsc = ?1 and studentRoles.roleEntity.role = ?2")
    fun findByItscAndRoles_Role(itsc: String, role: Role): Optional<StudentEntity>


    @Query(
        """select distinct s from StudentEntity s inner join s.enrolledSocietyRecordEntities enrolledSocietyRecordEntities inner join s.studentRoleEntities studentRoleEntities
where enrolledSocietyRecordEntities.societyEntity.name = ?1 and enrolledSocietyRecordEntities.status = ?2 and studentRoleEntities.societyEntity.name <> ?3"""
    )
    fun findAllStudentEnrolledInSocietyButNotSocietyMember(
        name: String,
        status: EnrolledStatus,
        name1: String
    ): List<StudentEntity>

    @Query(
        """select s from StudentEntity s inner join s.enrolledSocietyRecordEntities enrolledSocietyRecordEntity
where enrolledSocietyRecordEntity.societyEntity.name = ?1"""
    )
    fun findByEnrolledSocietyName(name: String): List<StudentEntity>


    @Query(
        """select s from StudentEntity s inner join s.enrolledSocietyRecordEntities enrolledSocietyRecordEntity
where s.uuid in ?1 and enrolledSocietyRecordEntity.societyEntity.name = ?2 and enrolledSocietyRecordEntity.status = ?3"""
    )
    fun findByIdInAndEnrolledSocietyNameAndEnrollStatus(
        uuids: MutableCollection<UUID>,
        name: String,
        status: EnrolledStatus
    ): List<StudentEntity>


    @Query("select s from StudentEntity s where s.cardId = ?1")
    fun findByCardId(cardId: String): Optional<StudentEntity>
}