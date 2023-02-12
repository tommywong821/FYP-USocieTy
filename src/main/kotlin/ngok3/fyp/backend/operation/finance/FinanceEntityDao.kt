package ngok3.fyp.backend.operation.finance

import org.apache.commons.lang3.StringUtils
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.Query

@Repository
class FinanceEntityDao(
    @PersistenceContext
    private val em: EntityManager
) {

    fun findFinanceTableDataWithSocietyNameWithPageAngFilter(
        societyName: String,
        fromDate: LocalDateTime,
        toDate: LocalDateTime,
        pageIndex: Int,
        pageSize: Int,
        sortField: String,
        isAscend: Boolean
    ): List<FinanceEntity>? {
        val sql: StringBuilder =
            StringBuilder("SELECT * FROM finance f, society s WHERE f.society_uuid = s.uuid AND s.name =:societyName AND f.date >= :fromDate AND f.date <= :toDate")
        //        custom sorting
        if (!StringUtils.isBlank(sortField)) {
            sql.append(" ORDER BY :sortField ${if (isAscend) "ASC" else "DESC"}")
        } else {
//            default sort by date
            sql.append(" ORDER BY f.date")
        }
        //        add pagination
        sql.append(" LIMIT :pageSize OFFSET :pageIndex")

        val query: Query = em.createNativeQuery(sql.toString(), FinanceEntity::class.java)

        query.setParameter("societyName", societyName)
        query.setParameter("fromDate", fromDate)
        query.setParameter("toDate", toDate)
        //        custom sorting
        if (!StringUtils.isBlank(sortField)) {
            query.setParameter("sortField", sortField)
        }
        query.setParameter("pageSize", pageSize)
        query.setParameter("pageIndex", (pageIndex - 1) * pageSize)

        return query.resultList?.filterIsInstance<FinanceEntity>()
    }
}