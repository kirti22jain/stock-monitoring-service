package stock.monitoring.service.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import stock.monitoring.service.manager.StockManager
import stock.monitoring.service.model.Stock
import stock.monitoring.service.model.StockData

@RestController
class StockController(
    private val stockManager: StockManager,
) {

    @PostMapping("/v1/add_stock_symbol")
    suspend fun addStockSymbol(@RequestParam stockSymbol: String) {
        stockManager.insertStock(stockSymbol)
    }

    @GetMapping("/v1/get_all_stocks")
    suspend fun getAllStocks(): List<Stock> {
        return stockManager.getAllStocks()
    }

    @PutMapping("/v1/update_stock")
    suspend fun updateStock(symbol: String, price: String, volume: String? = null, timestamp: String) {
        val stock: Stock = stockManager.getStock(symbol)
        val newVolume = volume ?: stock.stockData.volume
        stockManager.updateStock(stock, StockData(price, newVolume), timestamp)
    }
}
