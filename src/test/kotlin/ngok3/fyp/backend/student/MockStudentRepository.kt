package ngok3.fyp.backend.student

class MockStudentRepository {
    val testItsc: String = "test_itsc"
    val testStudentEntity: StudentEntity = StudentEntity(testItsc, "test_name", "test_mail", "test_role")
}