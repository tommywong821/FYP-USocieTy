package ngok3.fyp.backend.util

import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Component
class DateUtil {
    val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("M/d/yyyy")
    val formatterWithTimeStamp: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX")
    val currentLocalDateTime: LocalDateTime = LocalDateTime.now()
    fun convertLocalDateTimeToString(localDateTime: LocalDateTime): String {
        return localDateTime.format(formatter)
    }

    fun convertStringToLocalDateTime(dateString: String): LocalDateTime {
        return LocalDate.parse(dateString, formatter).atStartOfDay()
    }

    fun convertStringWithTimeStampToLocalDateTime(dateString: String): LocalDateTime {
        return LocalDateTime.parse(dateString, formatterWithTimeStamp)
    }
}