package com.example.DemoOpenFeign

import org.springframework.data.jpa.repository.JpaRepository


interface VacancyRepository : JpaRepository<Vacancy, Long> {
    fun findByHhId(hhId: String): Vacancy?
    fun findByHhIdIn(hhIds: List<String>): List<Vacancy>
}

interface AreaRepository : JpaRepository<AreaEntity, Long> {
    fun findByHhId(hhId: String): AreaEntity?
    fun findByHhIdIn(hhIds: List<String>): List<AreaEntity>
}

interface EmployerRepository : JpaRepository<Employer, Long> {
    fun findByHhId(hhId: String): Employer?
    fun findByHhIdIn(hhIds: List<String>): List<Employer>
}

interface TelegramUserRepository : JpaRepository<TelegramUserEntity, Long> {
    fun findByTelegramId(telegramId: Long): TelegramUserEntity?
}

