package ngok3.fyp.backend.operation.attendance;

import org.springframework.data.repository.CrudRepository

interface AttendanceRepository : CrudRepository<AttendanceEntity, AttendanceKey> {
}