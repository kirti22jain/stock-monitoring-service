package stock.monitoring.service.configuration

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.ServerApi
import com.mongodb.ServerApiVersion
import com.mongodb.connection.ConnectionPoolSettings
import com.mongodb.kotlin.client.coroutine.MongoClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import stock.monitoring.service.DBConstants.CONNECTION_STRING
import stock.monitoring.service.DBConstants.MAX_DB_POOL_SIZE
import stock.monitoring.service.DBConstants.MIN_DB_POOL_SIZE

@Configuration
class DBConfiguration {

    @Bean
    fun connect(): MongoClient {
        val serverApi = ServerApi.builder()
            .version(ServerApiVersion.V1)
            .build()
        val settings = MongoClientSettings.builder()
            .retryWrites(true)
            .applyConnectionString(ConnectionString(CONNECTION_STRING))
            .applyToConnectionPoolSettings { builder: ConnectionPoolSettings.Builder ->
                builder.maxSize(MAX_DB_POOL_SIZE).minSize(MIN_DB_POOL_SIZE) // connections count
            }
            .serverApi(serverApi)
            .build()

        return MongoClient.create(settings)
    }
}
