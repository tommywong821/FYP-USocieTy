package ngok3.fyp.backend.operation.society

import ngok3.fyp.backend.authentication.student_role.StudentRoleEntity
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordEntity
import ngok3.fyp.backend.operation.event.EventEntity
import ngok3.fyp.backend.operation.finance.FinanceEntity
import ngok3.fyp.backend.util.entity.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "society")
open class SocietyEntity(
    open var name: String = "",
    @Column(columnDefinition = "TEXT") open var description: String = "",
) : BaseEntity() {

    @OneToMany(mappedBy = "societyEntity")
    open var enrolledSocietyRecordEntities: MutableSet<EnrolledSocietyRecordEntity> = mutableSetOf()

    @OneToMany(mappedBy = "societyEntity")
    open var financeRecords: MutableSet<FinanceEntity> = mutableSetOf()

    @OneToMany(mappedBy = "societyEntity")
    open var eventRecords: MutableSet<EventEntity> = mutableSetOf()

    @OneToMany(mappedBy = "societyEntity")
    open var studentRoleEntities: MutableSet<StudentRoleEntity> = mutableSetOf()
}