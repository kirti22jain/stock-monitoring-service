package stock.monitoring.service.model

import org.bson.codecs.pojo.annotations.BsonProperty

data class StockData(
    @BsonProperty("price") val price: String,
    @BsonProperty("volume") val volume: String,
)
