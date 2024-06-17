package stock.monitoring.service.manager

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import stock.monitoring.service.Default.EMAIL_INTERVAL_IN_HOURS
import stock.monitoring.service.Default.FIXED_DELAY
import stock.monitoring.service.Default.INITIAL_DELAY
import stock.monitoring.service.model.Subscription
import stock.monitoring.service.model.request.SubscriptionRequest
import stock.monitoring.service.respository.SubscriptionRepository
import stock.monitoring.service.service.NotificationService
import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@Component
class SubscriptionManager(
    private val subscriptionRepository: SubscriptionRepository,
    private val stockManager: StockManager,
    private val notificationService: NotificationService,
) {

    private val logger = LoggerFactory.getLogger(SubscriptionManager::class.java)
    suspend fun subscribe(subscriptionRequest: SubscriptionRequest) {
        subscriptionRequest.symbols.forEach { symbol ->
            val stockData = stockManager.getLatestStockData(symbol)
            val subscription = Subscription(
                id = ObjectId().toHexString(),
                symbol = symbol,
                userId = subscriptionRequest.userId,
                lastNotifiedAt = stockData.first,
                lastNotifiedPrice = stockData.second.price,
            )
            subscriptionRepository.insert(subscription)
        }
    }

    @Scheduled(fixedRate = FIXED_DELAY, initialDelay = INITIAL_DELAY)
    fun checkAndNotify() {
        CoroutineScope(Dispatchers.Default).launch {
            logger.info("Checking and notifying users")
            val stocks = stockManager.getAllStocks()

            stocks.forEach { stock ->
                val latestStockData = stockManager.getLatestStockData(stock.symbol)
                val stockData = if (latestStockData.second.price != stock.stockData.price) {
                    // update stock data
                    stockManager.updateStock(stock, latestStockData.second, latestStockData.first)
                    logger.info("Stock data updated for ${stock.symbol}")
                    latestStockData.second
                } else {
                    stock.stockData
                }

                // get user-subscriptions for the stock
                val subscriptions: List<Subscription> = subscriptionRepository.getStockSubscriptions(stock.symbol)
                    .filter { subscription ->
                        subscription.symbol == stock.symbol && subscription.lastNotifiedPrice != stockData.price
                    }.filter { subscription ->
                        subscription.lastNotifiedAt?.let { isOneHourAgo(it, stock.lastUpdatedAt) } ?: false
                    }.map { subscription ->
                        subscription.copy(lastNotifiedPrice = stockData.price, lastNotifiedAt = stock.lastUpdatedAt)
                    }

                val userSubscriptionMap = subscriptions.groupBy { subscription -> subscription.userId }

                // update subscription last notified price and timestamp
                subscriptions.forEach { subscription -> subscriptionRepository.update(subscription) }

                // notify users
                notificationService.sendNotification(userSubscriptionMap)
            }
        }
    }

    suspend fun update(subscription: Subscription) {
        subscriptionRepository.update(subscription)
        logger.info("Subscription updated for ${subscription.symbol}")
    }

    private fun parseTimestampString(timestamp: String): Instant {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

        // Parse the string to a LocalDateTime
        val localDateTime = LocalDateTime.parse(timestamp, formatter)

        // Convert the LocalDateTime to an Instant
        return localDateTime.atZone(ZoneId.of("US/Eastern")).toInstant()
    }

    private fun isOneHourAgo(timestamp1: String, timestamp2: String): Boolean {
        val duration = Duration.between(parseTimestampString(timestamp1), parseTimestampString(timestamp2))

        // Check if the duration is at least 1 hour
        return duration >= Duration.ofHours(EMAIL_INTERVAL_IN_HOURS)
    }
}
