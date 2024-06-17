package stock.monitoring.service.controllers

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import stock.monitoring.service.controller.StockController
import stock.monitoring.service.manager.StockManager
import stock.monitoring.service.model.Stock
import stock.monitoring.service.model.StockData

class StockControllerUnitTests {

    private lateinit var stockController: StockController
    private lateinit var stockManager: StockManager

    @BeforeEach
    fun setUp() {
        stockManager = mockk()
        stockController = StockController(stockManager)
    }

    @Test
    fun testAddStockSymbol() = runBlocking {
        coEvery { stockManager.insertStock("AAPL") } returns Unit
        stockController.addStockSymbol("AAPL")
    }

    fun getStock(): Stock {
        return Stock(
            id = "123",
            symbol = "AAPL",
            name = "Apple",
            assetType = "CommonStock",
            stockData = StockData("100", "1000"),
            lastUpdatedAt = "2021-09-01T10:00:00Z",
        )
    }
}
