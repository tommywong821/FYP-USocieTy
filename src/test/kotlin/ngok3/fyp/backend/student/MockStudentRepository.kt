package ngok3.fyp.backend.student

class MockStudentRepository {
    val testStudentEntityItsc: String = "test_itsc"
    val testStudentEntity: StudentEntity = StudentEntity(testStudentEntityItsc, "test_name", "test_mail", "test_role")
}