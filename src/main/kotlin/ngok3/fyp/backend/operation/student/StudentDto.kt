package ngok3.fyp.backend.operation.student

import java.io.Serializable

/**
 * A DTO for the {@link ngok3.fyp.backend.student.StudentEntity} entity
 */
data class StudentDto(
    val itsc: String? = "",
    val nickname: String? = "",
    val mail: String? = "",
    val enrolledSocieties: String? = ""
) : Serializable {
    constructor(studentEntity: StudentEntity, enrolledSocietyList: String?) : this(
        studentEntity.itsc, studentEntity.nickname, studentEntity.mail, enrolledSocieties = enrolledSocietyList
    )
}