package com.example.DemoOpenFeign



object EntityMapper {

    fun toAreaEntity(dto: AreaDto): AreaEntity =
        AreaEntity(hhId = dto.id, name = dto.name, url = dto.url)

    fun toEmployerEntity(dto: com.example.DemoOpenFeign.HhEmployerDto): Employer =
        Employer(
            hhId = dto.id ?: throw IllegalArgumentException("Employer ID is null"),
            name = dto.name,

        )

    fun toVacancyEntity(dto: HhVacancyDto, employer: Employer?, area: AreaEntity?): Vacancy =
        Vacancy(
            hhId = dto.id,
            name = dto.name,
            url = dto.alternate_url ?: dto.url,
            employer = employer,
            area = area
        )
}