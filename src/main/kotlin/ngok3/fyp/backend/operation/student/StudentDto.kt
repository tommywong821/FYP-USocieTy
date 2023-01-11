package ngok3.fyp.backend.operation.student

import java.io.Serializable

/**
 * A DTO for the {@link ngok3.fyp.backend.student.StudentEntity} entity
 */
data class StudentDto(
    val itsc: String? = "",
    val nickname: String? = "",
    val mail: String? = "",
    val role: String? = ""
) : Serializable {
    constructor(studentEntity: StudentEntity) : this(
        studentEntity.itsc, studentEntity.nickname, studentEntity.mail, studentEntity.role
    )
}