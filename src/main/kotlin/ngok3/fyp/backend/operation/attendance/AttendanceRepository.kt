package ngok3.fyp.backend.operation.attendance

import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.transaction.annotation.Transactional

interface AttendanceRepository : CrudRepository<AttendanceEntity, AttendanceKey> {


    @Transactional
    @Modifying
    @Query("delete from AttendanceEntity a where a.attendanceKey = ?1")
    fun deleteByAttendanceKey(attendanceKey: AttendanceKey): Int

}