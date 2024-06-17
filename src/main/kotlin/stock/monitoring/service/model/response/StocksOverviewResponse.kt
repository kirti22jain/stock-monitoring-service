package stock.monitoring.service.model.response

import com.fasterxml.jackson.annotation.JsonProperty

data class StocksOverviewResponse(
    @JsonProperty("Symbol") val symbol: String,
    @JsonProperty("Name") val name: String,
    @JsonProperty("AssetType") val assetType: String,
)
