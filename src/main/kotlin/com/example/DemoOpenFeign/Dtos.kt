package com.example.DemoOpenFeign

data class AreaDto(
    val id: String,
    val name: String,
    val url: String
)



data class HhVacancyDto(
    val id: String,
    val premium: Boolean?,
    val name: String?,
    val department: HhDepartmentDto?,
    val has_test: Boolean?,
    val response_letter_required: Boolean?,
    val area: AreaDto?,
    val salary: HhSalaryDto?,
    val salary_range: Any?, // rarely used by HH, can be removed
    val type: HhVacancyTypeDto?,
    val address: HhAddressDto?,
    val published_at: String?,
    val created_at: String?,
    val archived: Boolean?,
    val apply_alternate_url: String?,
    val url: String?,
    val alternate_url: String?,
    val employer: HhEmployerDto?,
    val snippet: HhSnippetDto?,
    val schedule: HhScheduleDto?,
    val work_format: List<HhWorkFormatDto>?,
    val working_hours: List<HhWorkingHoursDto>?,
    val work_schedule_by_days: List<HhWorkScheduleByDaysDto>?,
    val night_shifts: Boolean?,
)


data class HhDepartmentDto(
    val id: String?,
    val name: String?
)


data class HhSalaryDto(
    val from: Int?,
    val to: Int?,
    val currency: String?,
    val gross: Boolean?
)

data class HhAddressDto(
    val city: String?,
    val street: String?,
    val building: String?,
    val lat: Double?,
    val lng: Double?,
    val description: String?
)

data class HhVacancyTypeDto(
    val id: String?,
    val name: String?
)


data class HhSnippetDto(
    val requirement: String?,
    val responsibility: String?
)


data class HhScheduleDto(
    val id: String?,
    val name: String?
)

data class HhWorkFormatDto(
    val id: String?,
    val name: String?
)

data class HhWorkingHoursDto(
    val id: String?,
    val name: String?
)

data class HhWorkScheduleByDaysDto(
    val id: String?,
    val name: String?
)



data class HhEmployerDto(
    val id: String?,
    val name: String?,
    val url: String?,
    val alternate_url: String?,
    val vacancies_url: String?,
    val accredited_it_employer: Boolean?,
    val trusted: Boolean?
)








data class RawVacancyResponse(
    val id: String,
    val name: String,
    val area: RawArea?,
    val employer: RawEmployer?,
    val published_at: String?,
    val url: String?,
    val schedule: RawSchedule?,
    val experience: RawExperience?,
    val salary: RawSalary?,
    val snippet: RawSnippet?,
    val work_format: List<RawWorkFormat>?
)

data class RawArea(
    val id: String,
    val name: String,
    val url: String
)

data class RawEmployer(
    val name: String?
)

data class RawSchedule(
    val name: String?
)

data class RawExperience(
    val name: String?
)

data class RawSalary(
    val from: Int?,
    val to: Int?,
    val currency: String?
)

data class RawSnippet(
    val requirement: String?,
    val responsibility: String?
)

data class RawWorkFormat(
    val id: String,
    val name: String
)
