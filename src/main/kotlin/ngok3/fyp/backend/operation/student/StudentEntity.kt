package ngok3.fyp.backend.operation.student

import ngok3.fyp.backend.authentication.student_role.StudentRoleEntity
import ngok3.fyp.backend.operation.attendance.AttendanceEntity
import ngok3.fyp.backend.operation.enrolled.event_record.EnrolledEventRecordEntity
import ngok3.fyp.backend.operation.enrolled.society_record.EnrolledSocietyRecordEntity
import ngok3.fyp.backend.operation.finance.FinanceEntity
import ngok3.fyp.backend.util.entity.BaseEntity
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "student")
open class StudentEntity(
    open var itsc: String = "",
    open var nickname: String = "",
    open var mail: String = "",
) : BaseEntity() {
    @OneToMany(mappedBy = "studentEntity")
    open var enrolledEventRecordEntities: MutableSet<EnrolledEventRecordEntity> = mutableSetOf()

    @OneToMany(mappedBy = "studentEntity")
    open var enrolledSocietyRecordEntities: MutableSet<EnrolledSocietyRecordEntity> = mutableSetOf()

    @OneToMany(mappedBy = "studentEntity")
    open var studentRoleEntities: MutableSet<StudentRoleEntity> = mutableSetOf()

    @OneToMany(mappedBy = "studentEntity")
    open var financeRecords: MutableSet<FinanceEntity> = mutableSetOf()

    @OneToMany(mappedBy = "studentEntity", orphanRemoval = true)
    open var attendanceEntities: MutableSet<AttendanceEntity> = mutableSetOf()
}