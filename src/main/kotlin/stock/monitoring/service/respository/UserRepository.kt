package stock.monitoring.service.respository

import com.google.gson.Gson
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.first
import org.bson.Document
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import stock.monitoring.service.DBConstants
import stock.monitoring.service.model.User

@Repository
class UserRepository(
    mongoClient: MongoClient,
//    private val bCryptPasswordEncoder: BCryptPasswordEncoder,
) {

    private val gson = Gson()
    private val logger = LoggerFactory.getLogger(UserRepository::class.java)
    private val database: MongoDatabase = mongoClient.getDatabase(DBConstants.STOCKS_DATABASE)

    private val usersCollection by lazy { connectToUsersCollection() }

    suspend fun insert(user: User) {
//        user.password = bCryptPasswordEncoder.encode(user.password)
        val doc = Document(gson.fromJson<Map<String, Any>>(gson.toJson(user), MutableMap::class.java))
        val result = usersCollection.insertOne(doc)
        logger.info("User=${user.userId} inserted successfully, id=${result.insertedId}")
    }

    suspend fun get(userId: String): User {
        val query = Document(User::userId.name, userId)
        val user = usersCollection.find(query).first()
        logger.info("User=$userId: $user")
        return gson.fromJson(gson.toJson(user), User::class.java)
    }

    private fun connectToUsersCollection(): MongoCollection<Document> {
        return runCatching {
            val stocksCollection = database.getCollection<Document>(DBConstants.USER_COLLECTION)
            stocksCollection
        }.onFailure {
            logger.error("Error while fetching collection: $it")
        }.getOrThrow()
    }
}
