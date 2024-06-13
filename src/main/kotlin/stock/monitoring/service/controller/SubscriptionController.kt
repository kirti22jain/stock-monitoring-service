package stock.monitoring.service.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import stock.monitoring.service.manager.SubscriptionManager
import stock.monitoring.service.model.request.SubscriptionRequest

@RestController
class SubscriptionController(
    private val subscriptionManager: SubscriptionManager,
) {

    @PostMapping("/v1/subscribe")
    suspend fun subscribe(@RequestBody subscriptionRequest: SubscriptionRequest) {
        subscriptionManager.subscribe(subscriptionRequest)
    }

    @PostMapping("/v1/send_subscription_notification")
    suspend fun sendSubscriptionNotification() {
        subscriptionManager.checkStockPrices()
    }
}
