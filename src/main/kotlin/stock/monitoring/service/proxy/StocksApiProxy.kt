package stock.monitoring.service.proxy

import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import stock.monitoring.service.Default.GATEWAY_TIMEOUT
import stock.monitoring.service.Default.SERVER_ERROR
import stock.monitoring.service.ProxyConstants.STOCKS_API_KEY
import stock.monitoring.service.ProxyConstants.STOCKS_API_PATH
import stock.monitoring.service.ProxyConstants.STOCKS_INTERVAL
import stock.monitoring.service.exception.ProxyException
import stock.monitoring.service.model.response.StocksOverviewResponse
import stock.monitoring.service.model.response.TimeSeriesResponse
import java.time.Duration

@Component
class StocksApiProxy(
    @Value("\${webhookProxy.timeout}") private val webhookTimeout: Long,
    @Value("\${stocks_api.base_url}") private val baseUrl: String,
) {

    private val logger = LoggerFactory.getLogger(StocksApiProxy::class.java)
    private val webClient = WebClient.builder()
        .baseUrl(baseUrl)
        .build()

    suspend fun getStockOverview(stockSymbol: String): StocksOverviewResponse {
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path(STOCKS_API_PATH)
                    .queryParam("function", "OVERVIEW")
                    .queryParam("symbol", stockSymbol)
                    .queryParam("apikey", STOCKS_API_KEY)
                    .build()
            }
            .retrieve()
            .bodyToMono(StocksOverviewResponse::class.java)
            .timeout(
                Duration.ofMillis(webhookTimeout),
                Mono.error(ProxyException("Timeout while calling webhook", GATEWAY_TIMEOUT)),
            )
            .awaitSingleOrNull() ?: throw ProxyException("Error while fetching stock overview", SERVER_ERROR)
        logger.info("Stock overview for $stockSymbol: $response")
        return response
    }

    suspend fun getIntradayStockData(stockSymbol: String): TimeSeriesResponse {
        val response = webClient.get()
            .uri { uriBuilder ->
                uriBuilder.path(STOCKS_API_PATH)
                    .queryParam("function", "TIME_SERIES_INTRADAY")
                    .queryParam("symbol", stockSymbol)
                    .queryParam("interval", STOCKS_INTERVAL)
                    .queryParam("apikey", STOCKS_API_KEY)
                    .build()
            }
            .retrieve()
            .bodyToMono(TimeSeriesResponse::class.java)
            .timeout(
                Duration.ofMillis(webhookTimeout),
                Mono.error(ProxyException("Timeout while calling webhook", GATEWAY_TIMEOUT)),
            )
            .awaitSingleOrNull() ?: throw ProxyException("Error while fetching intraday stock data", SERVER_ERROR)
        logger.info("Intraday stock data for $stockSymbol: $response")
        return response
    }
}
