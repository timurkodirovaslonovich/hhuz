package com.example.DemoOpenFeign

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "telegram.bot")
data class TelegramProperties(
    var token: String = "",
    var username: String = ""
)
