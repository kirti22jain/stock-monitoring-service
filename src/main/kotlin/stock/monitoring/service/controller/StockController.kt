package stock.monitoring.service.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import stock.monitoring.service.model.Stock
import stock.monitoring.service.service.StockService

@RestController
class StockController(
    private val stockService: StockService,
) {

    @PostMapping("/v1/add_stock_symbol")
    suspend fun addStockSymbol(@RequestParam stockSymbol: String) {
        stockService.insertStock(stockSymbol)
    }

    @GetMapping("/v1/get_all_stocks")
    suspend fun getAllStocks(): List<Stock> {
        return stockService.getAllStocks()
    }
}
