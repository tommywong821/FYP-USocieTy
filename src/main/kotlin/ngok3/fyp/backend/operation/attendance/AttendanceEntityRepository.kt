package ngok3.fyp.backend.operation.attendance

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface AttendanceEntityRepository : CrudRepository<AttendanceEntity, AttendanceKey> {


    @Transactional
    @Modifying
    @Query("delete from AttendanceEntity a where a.attendanceKey = ?1")
    fun deleteByAttendanceKey(attendanceKey: AttendanceKey): Int


    @Query("select a from AttendanceEntity a where a.attendanceKey.eventUuid = ?1 order by a.createdAt ")
    fun findByAttendanceKey_EventUuid(eventUuid: UUID, pageable: Pageable): List<AttendanceEntity>


    @Query("select count(a) from AttendanceEntity a where a.attendanceKey.eventUuid = ?1")
    fun countByAttendanceKey_EventUuid(eventUuid: UUID): Long

}