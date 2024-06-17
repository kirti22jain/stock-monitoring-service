package stock.monitoring.service.manager

import org.springframework.stereotype.Component
import stock.monitoring.service.model.User
import stock.monitoring.service.respository.UserRepository
import java.util.*

@Component
class UserManager(
    private val userRepository: UserRepository,
) {

    suspend fun addUser(user: User) {
        val finalUser = if (user.id == null) {
            user.copy(id = UUID.randomUUID().toString())
        } else {
            user
        }
        userRepository.insert(finalUser)
    }

    suspend fun getUser(userId: String): User {
        return userRepository.get(userId)
    }
}
