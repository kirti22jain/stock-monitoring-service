package stock.monitoring.service.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import org.bson.codecs.pojo.annotations.BsonId
import org.bson.codecs.pojo.annotations.BsonProperty

data class Stock(
    @JsonIgnore
    @BsonId
    val id: String? = null,

    @JsonProperty("symbol")
    @BsonProperty(value = "symbol")
    val symbol: String,

    @JsonProperty("name")
    @BsonProperty(value = "name")
    val name: String,

    @JsonProperty("asset_type")
    @BsonProperty(value = "assetType")
    val assetType: String,

    @JsonProperty("stock_data")
    @BsonProperty(value = "stockData")
    val stockData: StockData,

    @JsonProperty("last_updated_at")
    @BsonProperty(value = "lastUpdatedAt")
    val lastUpdatedAt: String,
)
