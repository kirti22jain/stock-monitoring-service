package stock.monitoring.service.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty

data class Subscription(
    @BsonId val id: String? = null,
    @BsonProperty(value = "user_id") val userId: String,
    @BsonProperty(value = "symbol") val symbol: String,
    @BsonProperty(value = "last_notified_at") var lastNotifiedAt: String? = null,
    @BsonProperty(value = "last_notified_price") var lastNotifiedPrice: String? = null,
)
