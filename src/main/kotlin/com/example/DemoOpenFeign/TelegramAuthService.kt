package com.example.DemoOpenFeign


import org.springframework.stereotype.Service
import java.security.MessageDigest
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

@Service
class TelegramAuthService(
    private val telegramProperties: TelegramProperties
) {

    fun verifyTelegramAuth(params: Map<String, String>): Boolean {
        val hash = params["hash"] ?: return false
        val authDate = params["auth_date"]?.toLongOrNull() ?: return false

        // Check if auth is not too old (24 hours)
        val currentTime = System.currentTimeMillis() / 1000
        if (currentTime - authDate > 86400) {
            return false
        }


        val dataCheckString = params
            .filter { it.key != "hash" }
            .toSortedMap()
            .map { "${it.key}=${it.value}" }
            .joinToString("\n")


        val secretKey = sha256(telegramProperties.token.toByteArray())
        val calculatedHash = hmacSha256(dataCheckString.toByteArray(), secretKey)


        return calculatedHash.equals(hash, ignoreCase = true)
    }

    private fun sha256(data: ByteArray): ByteArray {
        val digest = MessageDigest.getInstance("SHA-256")
        return digest.digest(data)
    }

    private fun hmacSha256(data: ByteArray, key: ByteArray): String {
        val mac = Mac.getInstance("HmacSHA256")
        val secretKey = SecretKeySpec(key, "HmacSHA256")
        mac.init(secretKey)
        val result = mac.doFinal(data)
        return result.joinToString("") { "%02x".format(it) }
    }
}