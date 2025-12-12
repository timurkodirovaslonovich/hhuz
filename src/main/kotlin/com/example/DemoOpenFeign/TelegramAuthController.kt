package com.example.DemoOpenFeign


import jakarta.servlet.http.HttpSession
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.view.RedirectView

@Controller
class TelegramAuthController(
    private val telegramAuthService: TelegramAuthService,
    private val telegramProperties: TelegramProperties
) {
    private val logger = LoggerFactory.getLogger(TelegramAuthController::class.java)

    // Login page
    @GetMapping("/auth/telegram/login")
    fun loginPage(model: Model): String {
        model.addAttribute("botUsername", telegramProperties.username)
        return "telegram-login"
    }

    // Callback endpoint
    @GetMapping("/auth/telegram/callback")
    fun handleCallback(
        @RequestParam allParams: Map<String, String>,
        session: HttpSession
    ): RedirectView {
        logger.info("Telegram callback received")

        return try {
            val isValid = telegramAuthService.verifyTelegramAuth(allParams)

            if (isValid) {
                val telegramId = allParams["id"]?.toLongOrNull()
                val firstName = allParams["first_name"]
                val lastName = allParams["last_name"]
                val username = allParams["username"]
                val photoUrl = allParams["photo_url"]

                logger.info("Telegram auth successful for user: $username (ID: $telegramId)")

                // Store in session
                session.setAttribute("telegramId", telegramId)
                session.setAttribute("telegramUsername", username)
                session.setAttribute("telegramFirstName", firstName)
                session.setAttribute("telegramLastName", lastName)
                session.setAttribute("telegramPhotoUrl", photoUrl)
                session.setAttribute("authenticated", true)

                // Redirect to dashboard at root level
                RedirectView("/dashboard")
            } else {
                logger.warn("Telegram auth verification failed")
                RedirectView("/auth/telegram/login?error=verification_failed")
            }
        } catch (e: Exception) {
            logger.error("Error during Telegram authentication", e)
            RedirectView("/auth/telegram/login?error=auth_failed")
        }
    }

    // Dashboard at root level - MOVED OUTSIDE @RequestMapping
    @GetMapping("/dashboard")
    fun dashboard(session: HttpSession, model: Model): String {
        val authenticated = session.getAttribute("authenticated") as? Boolean ?: false

        if (!authenticated) {
            return "redirect:/auth/telegram/login"
        }

        model.addAttribute("username", session.getAttribute("telegramUsername"))
        model.addAttribute("firstName", session.getAttribute("telegramFirstName"))
        model.addAttribute("photoUrl", session.getAttribute("telegramPhotoUrl"))

        return "dashboard"
    }

    // Logout
    @GetMapping("/auth/telegram/logout")
    fun logout(session: HttpSession): RedirectView {
        session.invalidate()
        return RedirectView("/auth/telegram/login")
    }
}
