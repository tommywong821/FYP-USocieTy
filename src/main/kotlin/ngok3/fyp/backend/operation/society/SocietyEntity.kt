package ngok3.fyp.backend.operation.society

import ngok3.fyp.backend.operation.enrolled_society_record.EnrolledSocietyRecordEntity
import ngok3.fyp.backend.operation.finance.FinanceEntity
import ngok3.fyp.backend.util.entity.BaseEntity
import javax.persistence.Entity
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "society")
open class SocietyEntity(
    open var name: String? = "",

    ) : BaseEntity() {

    @OneToMany(mappedBy = "societyEntity")
    open var enrolledSocietyRecordEntities: MutableSet<EnrolledSocietyRecordEntity> = mutableSetOf()

    @OneToMany(mappedBy = "societyEntity")
    open var financeRecords: MutableSet<FinanceEntity> = mutableSetOf()
}