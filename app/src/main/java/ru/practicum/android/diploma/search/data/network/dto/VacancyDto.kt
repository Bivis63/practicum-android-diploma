package ru.practicum.android.diploma.search.data.network.dto

data class VacancyDto(
    val accept_incomplete_resumes: Boolean,
    val accept_temporary: Boolean,
    val address: Any?,
    val adv_response_url: Any?,
    val alternate_url: String,
    val apply_alternate_url: String,
    val archived: Boolean,
    val area: AreaDto,
    val contacts: Any?,
    val created_at: String,
    val department: Any?,
    val employer: EmployerDto,
    val employment: EmploymentDto,
    val experience: ExperienceDto,
    val has_test: Boolean,
    val id: String,
    val insider_interview: Any?,
    val is_adv_vacancy: Boolean,
    val name: String,
    val premium: Boolean,
    val professional_roles: List<ProfessionalRoleDto>,
    val published_at: String,
    val relations: List<Any>,
    val response_letter_required: Boolean,
    val response_url: Any?,
    val salary: SalaryDto,
    val schedule: ScheduleDto,
    val show_logo_in_search: Any?,
    val snippet: SnippetDto,
    val sort_point_distance: Any?,
    val type: TypeDto,
    val url: String,
    val working_days: List<Any>,
    val working_time_intervals: List<Any>,
    val working_time_modes: List<Any>
)
