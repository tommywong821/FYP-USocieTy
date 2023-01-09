package ngok3.fyp.backend.student

import ngok3.fyp.backend.enrolled_event_record.EnrolledEventRecordEntity
import ngok3.fyp.backend.enrolled_society_record.EnrolledSocietyRecordEntity
import ngok3.fyp.backend.util.entity.BaseEntity
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "student")
open class StudentEntity(
    open var itsc: String? = null,
    open var nickname: String? = null,
    open var mail: String? = null,
    open var role: String? = null,

) : BaseEntity() {
    @OneToMany(mappedBy = "eventEntity")
    open var enrolledEventRecordEntity: MutableSet<EnrolledEventRecordEntity> = mutableSetOf()

    @OneToMany(mappedBy = "societyEntity")
    open var enrolledSocietyRecordEntity: MutableSet<EnrolledSocietyRecordEntity> = mutableSetOf()
}