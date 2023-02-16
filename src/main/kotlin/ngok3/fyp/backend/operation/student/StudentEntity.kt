package ngok3.fyp.backend.operation.student

import ngok3.fyp.backend.authentication.role.RoleEntity
import ngok3.fyp.backend.operation.enrolled_event_record.EnrolledEventRecordEntity
import ngok3.fyp.backend.operation.enrolled_society_record.EnrolledSocietyRecordEntity
import ngok3.fyp.backend.operation.finance.FinanceEntity
import ngok3.fyp.backend.util.entity.BaseEntity
import javax.persistence.*

@Entity
@Table(name = "student")
open class StudentEntity(
    open var itsc: String = "",
    open var nickname: String = "",
    open var mail: String = "",
) : BaseEntity() {
    @OneToMany(mappedBy = "studentEntity")
    open var enrolledEventRecordEntity: MutableSet<EnrolledEventRecordEntity> = mutableSetOf()

    @OneToMany(mappedBy = "studentEntity")
    open var enrolledSocietyRecordEntity: MutableSet<EnrolledSocietyRecordEntity> = mutableSetOf()

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "student_roles",
        joinColumns = [JoinColumn(name = "student_uuid")],
        inverseJoinColumns = [JoinColumn(name = "role_id")]
    )
    open var roles: MutableSet<RoleEntity> = mutableSetOf()

    @OneToMany(mappedBy = "studentEntity")
    open var financeRecords: MutableSet<FinanceEntity> = mutableSetOf()
}