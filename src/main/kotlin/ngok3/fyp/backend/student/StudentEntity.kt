package ngok3.fyp.backend.student

import ngok3.fyp.backend.enrolled_society_record.EnrolledSocietyRecordEntity
import ngok3.fyp.backend.util.entity.BaseEntity
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "student")
class StudentEntity(
    var itsc: String? = null,
    var nickname: String? = null,
    var mail: String? = null,
    var role: String? = null,

    @OneToMany(mappedBy = "studentEntity", orphanRemoval = true)
    var enrolledSocietyRecordEntities: MutableSet<EnrolledSocietyRecordEntity> = mutableSetOf()
) : BaseEntity() {

}