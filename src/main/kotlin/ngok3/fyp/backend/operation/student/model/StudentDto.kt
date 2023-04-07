package ngok3.fyp.backend.operation.student.model

import ngok3.fyp.backend.operation.student.StudentEntity
import java.io.Serializable
import java.time.LocalDateTime

/**
 * A DTO for the {@link ngok3.fyp.backend.student.StudentEntity} entity
 */
data class StudentDto(
    val uuid: String = "",
    val itsc: String = "",
    val nickname: String = "",
    val mail: String = "",
    val enrolledSocieties: List<String> = emptyList(),
    val roles: List<String> = emptyList(),
    val expireDate: LocalDateTime = LocalDateTime.now()
) : Serializable {
    constructor(studentEntity: StudentEntity, enrolledSocietyList: List<String>) : this(
        uuid = studentEntity.uuid.toString(),
        itsc = studentEntity.itsc,
        nickname = studentEntity.nickname,
        mail = studentEntity.mail,
        enrolledSocieties = enrolledSocietyList
    )

    constructor(studentEntity: StudentEntity, enrolledSocietyList: List<String>, roles: List<String>) : this(
        uuid = studentEntity.uuid.toString(),
        itsc = studentEntity.itsc,
        nickname = studentEntity.nickname,
        mail = studentEntity.mail,
        enrolledSocieties = enrolledSocietyList,
        roles = roles,
        expireDate = LocalDateTime.now().plusDays(1)
    )
}