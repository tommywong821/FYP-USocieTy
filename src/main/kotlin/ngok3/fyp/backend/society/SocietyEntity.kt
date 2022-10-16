package ngok3.fyp.backend.society

import ngok3.fyp.backend.enrolled_society_record.EnrolledSocietyRecordEntity
import ngok3.fyp.backend.util.entity.BaseEntity
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "society")
class SocietyEntity(
    var name: String? = null,

    @OneToMany(mappedBy = "societyEntity", orphanRemoval = true)
    var enrolledSocietyRecordEntities: MutableSet<EnrolledSocietyRecordEntity> = mutableSetOf()
) : BaseEntity()