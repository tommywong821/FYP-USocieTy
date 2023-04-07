package ngok3.fyp.backend.util

import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Component
class DateUtil {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy")
    val formatterWithHour: DateTimeFormatter = DateTimeFormatter.ofPattern("d/M/yyyy HH:mm")
    val formatterWithTimeStamp: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
    val currentLocalDateTime: LocalDateTime = LocalDateTime.now(ZoneId.of("Asia/Hong_Kong"))
    fun convertLocalDateTimeToString(localDateTime: LocalDateTime?): String {
        return if (localDateTime != null) localDateTime.format(formatter) else ""
    }

    fun convertLocalDateTimeToStringWithTime(localDateTime: LocalDateTime?): String {
        return if (localDateTime != null) localDateTime.format(formatterWithHour) else ""
    }

    fun convertStringToLocalDateTime(dateString: String): LocalDateTime {
        return LocalDate.parse(dateString, formatter).atStartOfDay()
    }

    fun convertStringWithTimeStampToLocalDateTime(dateString: String): LocalDateTime {
        return LocalDateTime.parse(dateString, formatterWithTimeStamp)
    }
}