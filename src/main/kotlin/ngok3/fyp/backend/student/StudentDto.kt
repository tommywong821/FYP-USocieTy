package ngok3.fyp.backend.student

import java.io.Serializable

/**
 * A DTO for the {@link ngok3.fyp.backend.student.StudentEntity} entity
 */
data class StudentDto(
    val itsc: String? = null,
    val name: String? = null,
    val mail: String? = null,
    val role: String? = null
) : Serializable