package ngok3.fyp.backend.operation.student

import java.io.Serializable

/**
 * A DTO for the {@link ngok3.fyp.backend.student.StudentEntity} entity
 */
data class StudentDto(
    val itsc: String = "",
    val nickname: String = "",
    val mail: String = "",
    val enrolledSocieties: List<String> = emptyList(),
    val roles: List<String> = emptyList()
) : Serializable {
    constructor(studentEntity: StudentEntity, enrolledSocietyList: List<String>) : this(
        studentEntity.itsc, studentEntity.nickname, studentEntity.mail, enrolledSocieties = enrolledSocietyList
    )

    constructor(studentEntity: StudentEntity, enrolledSocietyList: List<String>, roles: List<String>) : this(
        studentEntity.itsc,
        studentEntity.nickname,
        studentEntity.mail,
        enrolledSocieties = enrolledSocietyList,
        roles = roles
    )
}