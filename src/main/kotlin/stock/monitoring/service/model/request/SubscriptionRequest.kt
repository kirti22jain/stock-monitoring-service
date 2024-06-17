package stock.monitoring.service.model.request

import com.fasterxml.jackson.annotation.JsonProperty

data class SubscriptionRequest(
    @JsonProperty("userId") val userId: String,
    @JsonProperty("symbols") val symbols: List<String>,
    @JsonProperty("emailSendInterval") val emailSendInterval: Int? = 60, // in minutes
)
