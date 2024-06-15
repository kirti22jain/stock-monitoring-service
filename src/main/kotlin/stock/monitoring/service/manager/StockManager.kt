package stock.monitoring.service.manager

import org.springframework.stereotype.Service
import stock.monitoring.service.model.Stock
import stock.monitoring.service.model.StockData
import stock.monitoring.service.proxy.StocksApiProxy
import stock.monitoring.service.respository.StockRepository

@Service
class StockManager(
    private val stockRepository: StockRepository,
    private val stocksApiProxy: StocksApiProxy,
) {

    suspend fun insertStock(stockSymbol: String) {
        val stockOverview = stocksApiProxy.getStockOverview(stockSymbol)
        val stockData = getLatestStockData(stockSymbol)
        val stock = Stock(
            symbol = stockOverview.symbol,
            name = stockOverview.name,
            assetType = stockOverview.assetType,
            stockData = stockData.second,
            lastUpdatedAt = stockData.first,
        )
        stockRepository.insert(stock)
    }

    suspend fun getAllStocks(): List<Stock> {
        return stockRepository.getAll()
    }

    suspend fun getLatestStockData(stockSymbol: String): Pair<String, StockData> {
        val timeSeriesResponse = stocksApiProxy.getIntradayStockData(stockSymbol)
        val latestTimeStamp = timeSeriesResponse.timeSeries.keys.first()

        timeSeriesResponse.timeSeries.getValue(latestTimeStamp).let { timeSeries ->
            val latestPrice = timeSeries.close
            val latestVolume = timeSeries.volume
            return Pair(
                latestTimeStamp,
                StockData(latestPrice, latestVolume),
            )
        }
    }

    suspend fun updateStock(stock: Stock, updatedStockData: StockData, updatedTimeStamp: String) {
        val updatedStock = stock.copy(stockData = updatedStockData, lastUpdatedAt = updatedTimeStamp)
        stockRepository.updateStock(updatedStock)
    }

    suspend fun getStock(stockSymbol: String): Stock {
        return stockRepository.getStock(stockSymbol)
    }
}
