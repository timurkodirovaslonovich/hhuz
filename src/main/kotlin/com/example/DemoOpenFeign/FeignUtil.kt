package com.example.DemoOpenFeign

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam


data class HhVacanciesSearchResponse(
    val items: List<RawVacancyResponse>
)

@FeignClient(name = "feign-service", url = "https://api.hh.ru")
interface FeignUtil {

    @GetMapping("/vacancies")
    fun searchVacancies(
        @RequestParam("text") text: String,
        @RequestParam("page") page: Int? = null,
        @RequestParam("per_page") perPage: Int? = null
    ): HhVacanciesSearchResponse

}