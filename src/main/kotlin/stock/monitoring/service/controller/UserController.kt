package stock.monitoring.service.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import stock.monitoring.service.Default.SHA_SECRET_KEY
import stock.monitoring.service.Default.TOKEN_EXPIRATION_IN_MILLIS
import stock.monitoring.service.Default.TOKEN_PREFIX
import stock.monitoring.service.manager.UserManager
import stock.monitoring.service.model.User
import java.time.Instant

@RestController
class UserController(
    private val userManager: UserManager,
) {

    @PostMapping("/v1/add_user")
    suspend fun addUser(@RequestBody user: User) {
        userManager.addUser(user)
    }

    @GetMapping("/v1/get_user")
    suspend fun getUser(userId: String): User {
        return userManager.getUser(userId)
    }

    /**
     * This method will be a part of the UI component and the token will be passed to the downstream services.
     * It is implemented here for the sake of simplicity and demo purpose.
     */
    @PostMapping("/v1/login")
    fun login(@RequestParam("userName") username: String, @RequestParam("password") pwd: String): String {
        println(pwd)
        return getJWTToken(username)
    }

    private fun getJWTToken(username: String): String {
        val token: String = JWT.create()
            .withSubject(username)
            .withIssuedAt(Instant.now())
            .withExpiresAt(Instant.now().plusMillis(TOKEN_EXPIRATION_IN_MILLIS))
            .withClaim("roles", listOf("user"))
            .sign(Algorithm.HMAC512(SHA_SECRET_KEY))
        return "${TOKEN_PREFIX}$token"
    }
}
