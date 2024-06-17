package stock.monitoring.service.respository

import com.google.gson.Gson
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.toList
import org.bson.Document
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import stock.monitoring.service.DBConstants
import stock.monitoring.service.model.Subscription

@Component
class SubscriptionRepository(
    mongoClient: MongoClient,
) {

    private val gson = Gson()
    private val logger = LoggerFactory.getLogger(SubscriptionRepository::class.java)
    private val database: MongoDatabase = mongoClient.getDatabase(DBConstants.STOCKS_DATABASE)
    private val subscriptionCollection by lazy { connectToSubscriptionCollection() }

    suspend fun getStockSubscriptions(symbol: String): List<Subscription> {
        return runCatching {
            val query = Filters.and(
                Filters.eq(Subscription::symbol.name, symbol),
            )
            val subscriptionDocuments = subscriptionCollection.find(query).toList()
            val subscriptions = mutableListOf<Subscription>()
            subscriptionDocuments.forEach {
                subscriptions.add(gson.fromJson(gson.toJson(it), Subscription::class.java))
            }

            logger.info("Eligible subscriptions: $subscriptions")
            subscriptions
        }.onFailure {
            logger.error("Error while fetching subscription: $it")
        }.getOrThrow()
    }

    suspend fun insert(subscription: Subscription) {
        runCatching {
            val subscriptionAsMap = gson.fromJson<Map<String, Any>>(gson.toJson(subscription), MutableMap::class.java)
            val doc = Document(subscriptionAsMap)
            subscriptionCollection.insertOne(doc)
            logger.info(
                "Subscription for userId=${subscription.userId}, symbol=${subscription.symbol} inserted successfully.",
            )
        }.onFailure {
            logger.error("Error while inserting stock: $it")
        }
    }

    suspend fun update(subscription: Subscription) {
        val query = Filters.eq(Subscription::symbol.name, subscription.symbol)
        val updates = Updates.combine(
            Updates.set(subscription::lastNotifiedPrice.name, subscription.lastNotifiedPrice),
            Updates.set(subscription::lastNotifiedAt.name, subscription.lastNotifiedAt),
        )
        val result = subscriptionCollection.updateOne(query, updates)
        logger.info("Updates=$result for subscription=$subscription")
    }

    private fun connectToSubscriptionCollection(): MongoCollection<Document> {
        return runCatching {
            val collection = database.getCollection<Document>(DBConstants.SUBSCRIPTION_COLLECTION)
            collection
        }.onFailure {
            logger.error("Error while fetching collection: $it")
        }.getOrThrow()
    }
}
