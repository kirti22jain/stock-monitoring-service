package stock.monitoring.service.service

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import stock.monitoring.service.model.Subscription

@Service
class NotificationService {

    private val logger = LoggerFactory.getLogger(NotificationService::class.java)
    fun sendNotification(userSubscriptionMap: Map<String, List<Subscription>>) {
        // Send notification to user
        userSubscriptionMap.forEach { (key, value) ->
            logger.info("Sending notification to user=$key, subscriptions=$value")
        }
    }
}
