package com.example.DemoOpenFeign

import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VacancySyncService(
    private val hhClient: FeignUtil,
    private val vacancyRepository: VacancyRepository,
    private val employerRepository: EmployerRepository,
    private val areaRepository: AreaRepository
) {
    private val logger = LoggerFactory.getLogger(VacancySyncService::class.java)
    //bu yerda hard code qilingan keyword dynamic input orqali olinmagan
    @Scheduled(fixedRate = 15 * 60 * 1000)
    @Transactional
    fun syncVacancies() {
        try {
            logger.info("Starting vacancy sync...")
            val response = hhClient.searchVacancies("Python Developer", perPage = 20)


            val vacancyIds = response.items.map { it.id }
            val existingVacancyIds = vacancyRepository.findByHhIdIn(vacancyIds)
                .map { it.hhId }
                .toSet()

            val newVacancies = response.items.filter { it.id !in existingVacancyIds }

            if (newVacancies.isEmpty()) {
                logger.info("No new vacancies to sync")
                return
            }

            logger.info("Found ${newVacancies.size} new vacancies to sync")


            val areas = fetchOrCreateAreas(newVacancies)


            val employers = fetchOrCreateEmployers(newVacancies)


            val entitiesToSave = newVacancies.mapNotNull { raw ->
                try {
                    val vacancyDto = HhMapper.toVacancyDto(raw)
                    val area = raw.area?.id?.let { areas[it] }
                    val employer = raw.employer?.name?.let { employers[it] }

                    EntityMapper.toVacancyEntity(vacancyDto, employer, area)
                } catch (e: Exception) {
                    logger.error("Error mapping vacancy ${raw.id}: ${raw.name}", e)
                    null
                }
            }

            if (entitiesToSave.isNotEmpty()) {
                vacancyRepository.saveAll(entitiesToSave)
                logger.info("Saved ${entitiesToSave.size} vacancy entities")
            }

            logger.info("Successfully synced ${newVacancies.size} new vacancies")
        } catch (e: Exception) {
            logger.error("Error during vacancy sync", e)
            throw e
        }
    }

    private fun fetchOrCreateAreas(vacancies: List<RawVacancyResponse>): Map<String, AreaEntity> {
        val areaIds = vacancies.mapNotNull { it.area?.id }.distinct()
        if (areaIds.isEmpty()) return emptyMap()


        val existing = areaRepository.findByHhIdIn(areaIds).associateBy { it.hhId }


        val missingAreaIds = areaIds - existing.keys

        if (missingAreaIds.isEmpty()) {
            return existing
        }


        val newAreas = vacancies
            .mapNotNull { raw ->
                raw.area?.let { area ->
                    if (area.id in missingAreaIds) {
                        EntityMapper.toAreaEntity(AreaDto(area.id, area.name, area.url))
                    } else null
                }
            }
            .distinctBy { it.hhId }

        val saved = if (newAreas.isNotEmpty()) {
            areaRepository.saveAll(newAreas).associateBy { it.hhId }
        } else {
            emptyMap()
        }

        logger.info("Areas - existing: ${existing.size}, created: ${saved.size}")
        return existing + saved
    }

    private fun fetchOrCreateEmployers(vacancies: List<RawVacancyResponse>): Map<String, Employer> {
        val employerNames = vacancies.mapNotNull { it.employer?.name }.distinct()
        if (employerNames.isEmpty()) return emptyMap()

        val existing = employerRepository.findByHhIdIn(employerNames).associateBy { it.hhId }


        val missingEmployerNames = employerNames - existing.keys

        if (missingEmployerNames.isEmpty()) {
            return existing
        }


        val newEmployers = missingEmployerNames.map { name ->
            EntityMapper.toEmployerEntity(
                HhEmployerDto(
                    id = name,
                    name = name,
                    url = null,
                    alternate_url = null,
                    vacancies_url = null,
                    accredited_it_employer = null,
                    trusted = null
                )
            )
        }

        val saved = if (newEmployers.isNotEmpty()) {
            employerRepository.saveAll(newEmployers).associateBy { it.hhId }
        } else {
            emptyMap()
        }

        logger.info("Employers - existing: ${existing.size}, created: ${saved.size}")
        return existing + saved
    }
}
