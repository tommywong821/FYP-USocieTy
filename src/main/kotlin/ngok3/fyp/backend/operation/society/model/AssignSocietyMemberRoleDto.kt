package ngok3.fyp.backend.operation.society.model

data class AssignSocietyMemberRoleDto(
    val societyName: String,
    val studentIdList: List<String>
)
