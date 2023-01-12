package ngok3.fyp.backend.student

import ngok3.fyp.backend.operation.student.StudentEntity

class MockStudentRepository {
    val testItsc: String = "test_itsc"
    val testStudentEntity: StudentEntity = StudentEntity(testItsc, "test_name", "test_mail")
}