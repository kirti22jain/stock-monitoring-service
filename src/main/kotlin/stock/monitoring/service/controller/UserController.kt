package stock.monitoring.service.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import stock.monitoring.service.manager.UserManager
import stock.monitoring.service.model.User

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
}
