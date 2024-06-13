package stock.monitoring.service

object DBConstants {
    const val STOCKS_LISTING_COLLECTION = "stocks_listing"
    const val STOCKS_DATABASE = "kj_stocks"
    const val SUBSCRIPTION_COLLECTION = "subscriptions"
    const val USER_COLLECTION = "users"
    const val CONNECTION_STRING = "mongodb+srv://Cluster19422:ZGloeU1kSUNl@cluster19422.ls8hmi5.mongodb.net/" +
        "?retryWrites=true&w=majority&appName=Cluster19422"
    const val MAX_DB_POOL_SIZE = 100
    const val MIN_DB_POOL_SIZE = 5
}

object Default {
    const val GATEWAY_TIMEOUT = 504
    const val SERVER_ERROR = 500
    const val LAST_NAME = "lastName"
    const val EMAIL = "email"
}

object ProxyConstants {
    const val STOCKS_API_PATH = "/query"
    const val STOCKS_API_KEY = "ZGloeU1kSUNl" // This is a dummy key
    const val STOCKS_INTERVAL = "15min"
}
