package ngok3.fyp.backend.operation.society

import io.swagger.v3.oas.annotations.Operation
import ngok3.fyp.backend.operation.society.model.AssignSocietyMemberRoleDto
import ngok3.fyp.backend.operation.society.model.JoinSocietyDto
import ngok3.fyp.backend.operation.student.StudentDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("society")
class SocietyController(
    private val societyService: SocietyService
) {
    @Operation(summary = "get all societies with pagination")
    @GetMapping
    fun getAllSocieties(
        @RequestParam("pageNum", required = false, defaultValue = "0") pageNum: Int,
        @RequestParam("pageSize", required = false, defaultValue = "10") pageSize: Int
    ): List<SocietyDto> {
        return societyService.getAllSocieties(pageNum, pageSize)
    }

    @Operation(summary = "join society with student itsc and society id")
    @PostMapping
    fun joinSociety(
        @RequestBody joinSocietyDto: JoinSocietyDto
    ): Boolean {
        return societyService.joinSociety(joinSocietyDto.itsc, joinSocietyDto.societyName)
    }

    @Operation(summary = "get all student of society")
    @GetMapping("/member")
    fun getAllSocietyMember(
        @RequestParam("societyName", required = true) societyName: String,
    ): List<StudentDto> {
        return societyService.getAllSocietyMember(societyName)
    }

    @Operation(summary = "assign society member role to student")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/member")
    fun assignSocietyMemberRole(
        @RequestBody assignSocietyMemberRoleDto: AssignSocietyMemberRoleDto,
    ) {
        return societyService.assignSocietyMemberRole(
            assignSocietyMemberRoleDto.societyName,
            assignSocietyMemberRoleDto.studentIdList
        )
    }

    @Operation(summary = "remove society member role from student")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @DeleteMapping("/member")
    fun removeSocietyMemberRole(
        @RequestParam("societyName") societyName: String,
        @RequestParam("id") deleteStudentIdList: List<String>
    ) {
        return societyService.removeSocietyMemberRole(
            societyName,
            deleteStudentIdList
        )
    }
}