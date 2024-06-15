package stock.monitoring.service.respository

import com.google.gson.Gson
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import com.mongodb.kotlin.client.coroutine.MongoClient
import com.mongodb.kotlin.client.coroutine.MongoCollection
import com.mongodb.kotlin.client.coroutine.MongoDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import org.bson.Document
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import stock.monitoring.service.DBConstants.STOCKS_DATABASE
import stock.monitoring.service.DBConstants.STOCKS_LISTING_COLLECTION
import stock.monitoring.service.model.Stock

@Repository
class StockRepository(
    mongoClient: MongoClient,
) {

    private val gson = Gson()
    private val logger = LoggerFactory.getLogger(StockRepository::class.java)
    private val database: MongoDatabase = mongoClient.getDatabase(STOCKS_DATABASE)

    private val stocksCollection by lazy { connectToStocksCollection() }

    suspend fun insert(stock: Stock) {
        runCatching {
            val doc = Document(gson.fromJson<Map<String, Any>>(gson.toJson(stock), MutableMap::class.java))
            val result = stocksCollection.insertOne(doc)
            logger.info("Stock=${stock.symbol} inserted successfully, id=${result.insertedId}")
        }.onFailure {
            logger.error("Error while inserting stock: $it")
        }
    }

    suspend fun getStock(stockSymbol: String): Stock {
        val stock = stocksCollection.find(Document(Stock::symbol.name, stockSymbol)).first()
        logger.info("Stock=$stockSymbol: $stock")
        return gson.fromJson(gson.toJson(stock), Stock::class.java)
    }

    suspend fun getAll(): List<Stock> {
        val stockDocuments = stocksCollection.find().toList()
        val stocks = mutableListOf<Stock>()
        stockDocuments.forEach { stocks.add(gson.fromJson(gson.toJson(it), Stock::class.java)) }
        logger.info("Stocks=$stocks")
        return stocks
    }

    suspend fun updateStock(updatedStockValue: Stock) {
        val query = Filters.eq(Stock::symbol.name, updatedStockValue.symbol)
        val updates = Updates.combine(
            Updates.set("stockData.volume", updatedStockValue.stockData.volume),
            Updates.set("stockData.price", updatedStockValue.stockData.price),
            Updates.set(Stock::lastUpdatedAt.name, updatedStockValue.lastUpdatedAt),
        )
        val result = stocksCollection.updateOne(query, updates)
        logger.info("Updates=$result for stock=${updatedStockValue.symbol}")
    }

    suspend fun delete(symbol: String) {
        // TODO delete
    }

    private fun connectToStocksCollection(): MongoCollection<Document> {
        return runCatching {
            val stocksCollection = database.getCollection<Document>(STOCKS_LISTING_COLLECTION)
            stocksCollection
        }.onFailure {
            logger.error("Error while fetching collection: $it")
        }.getOrThrow()
    }
}
