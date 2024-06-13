package stock.monitoring.service.manager

import org.bson.types.ObjectId
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import stock.monitoring.service.model.Subscription
import stock.monitoring.service.model.request.SubscriptionRequest
import stock.monitoring.service.respository.SubscriptionRepository
import stock.monitoring.service.service.NotificationService
import stock.monitoring.service.service.StockService
import java.time.Instant

@Component
class SubscriptionManager(
    private val subscriptionRepository: SubscriptionRepository,
    private val stockService: StockService,
    private val notificationService: NotificationService,
) {

    private val logger = LoggerFactory.getLogger(SubscriptionManager::class.java)
    suspend fun subscribe(subscriptionRequest: SubscriptionRequest) {
        subscriptionRequest.symbols.forEach { symbol ->
            val stockData = stockService.getLatestStockData(symbol)
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

    suspend fun checkStockPrices() {
        val stocks = stockService.getAllStocks()

        stocks.forEach { stock ->
            val latestStockData = stockService.getLatestStockData(stock.symbol)
            val stockData = if (latestStockData.second.price != stock.stockData.price) {
                // update stock data
                stockService.updateStock(stock, latestStockData.second, latestStockData.first)
                logger.info("Stock data updated for ${stock.symbol}")
                latestStockData.second
            } else {
                stock.stockData
            }

            // get user-subscriptions for the stock
            val subscriptions: List<Subscription> = subscriptionRepository.getEligibleSubscriptions(stock.symbol)
                .filter { subscription ->
                    subscription.symbol == stock.symbol && subscription.lastNotifiedPrice != stockData.price
                }
                .map { subscription ->
                    subscription.copy(lastNotifiedPrice = stockData.price, lastNotifiedAt = Instant.now().toString())
                }

            val userSubscriptionMap = subscriptions.groupBy { subscription -> subscription.userId }

            // update subscription last notified price and timestamp
            subscriptions.forEach { subscription -> subscriptionRepository.updateSubscription(subscription) }

            // notify users
            notificationService.sendNotification(userSubscriptionMap)
        }
    }

    /*private fun shouldNotify(subscription: Subscription): Boolean {
        val lastNotified = subscription.lastNotifiedAt ?: return true
        val oneHourAgo = Instant.now().minus(Duration.ofHours(1))
        val lastNotifiedInstant = Instant.parse(lastNotified)
        return lastNotifiedInstant.isBefore(oneHourAgo)
    }*/
}
