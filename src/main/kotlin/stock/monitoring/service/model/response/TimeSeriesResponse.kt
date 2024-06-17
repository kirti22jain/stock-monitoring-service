package stock.monitoring.service.model.response

import com.fasterxml.jackson.annotation.JsonProperty

data class TimeSeriesResponse(
    @JsonProperty("Meta Data") val metadata: Map<String, String>,
    @JsonProperty("Time Series (15min)") val timeSeries: Map<String, TimeSeriesDetails>,
)

data class TimeSeriesDetails(
    @JsonProperty("1. open") val open: String,
    @JsonProperty("2. high") val high: String,
    @JsonProperty("3. low") val low: String,
    @JsonProperty("4. close") val close: String,
    @JsonProperty("5. volume") val volume: String,
)
