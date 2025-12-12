package com.example.DemoOpenFeign



object HhMapper {

    fun toAreaDto(raw: RawArea?): AreaDto? {
        return raw?.let {
            AreaDto(
                id = it.id,
                name = it.name,
                url = it.url
            )
        }
    }

    fun toEmployerDto(raw: RawEmployer?): HhEmployerDto? {
        return raw?.let {
            HhEmployerDto(
                id = null,
                name = it.name,
                url = null,
                alternate_url = null,
                vacancies_url = null,
                accredited_it_employer = null,
                trusted = null
            )
        }
    }

    fun toScheduleDto(raw: RawSchedule?): HhScheduleDto? {
        return raw?.let {
            HhScheduleDto(
                id = null,
                name = it.name
            )
        }
    }

    fun toExperience(raw: RawExperience?): String? {
        return raw?.name
    }

    fun toSalaryDto(raw: RawSalary?): HhSalaryDto? {
        return raw?.let {
            HhSalaryDto(
                from = it.from,
                to = it.to,
                currency = it.currency,
                gross = null
            )
        }
    }

    fun toSnippetDto(raw: RawSnippet?): HhSnippetDto? {
        return raw?.let {
            HhSnippetDto(
                requirement = it.requirement,
                responsibility = it.responsibility
            )
        }
    }

    fun toWorkFormatDto(raw: RawWorkFormat): HhWorkFormatDto {
        return HhWorkFormatDto(
            id = raw.id,
            name = raw.name
        )
    }

    fun toVacancyDto(raw: RawVacancyResponse): HhVacancyDto {
        return HhVacancyDto(
            id = raw.id,
            premium = null,
            name = raw.name,
            department = null,
            has_test = null,
            response_letter_required = null,
            area = toAreaDto(raw.area),
            salary = toSalaryDto(raw.salary),
            salary_range = null,
            type = null,
            address = null,
            published_at = raw.published_at,
            created_at = null,
            archived = null,
            apply_alternate_url = null,
            url = raw.url,
            alternate_url = null,
            employer = toEmployerDto(raw.employer),
            snippet = toSnippetDto(raw.snippet),
            schedule = toScheduleDto(raw.schedule),
            work_format = raw.work_format?.map { toWorkFormatDto(it) },
            working_hours = null,
            work_schedule_by_days = null,
            night_shifts = null
        )
    }
}
