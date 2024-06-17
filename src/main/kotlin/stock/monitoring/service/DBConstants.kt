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
    const val EMAIL_INTERVAL_IN_HOURS: Long = 1
    const val FIXED_DELAY: Long = 60000
    const val INITIAL_DELAY: Long = 3000
    const val TOKEN_PREFIX = "Bearer "
    val ACCEPTED_ROLES = setOf("admin", "user")
    const val SHA_SECRET_KEY = "EncryptionAlgorithm16"
    const val AUTHORIZATION: String = "Authorization"
    val PROTECTED_ENDPOINTS = setOf(
        "/v1/subscribe",
    )
    const val HTTP_OPTIONS = "options"
    const val TOKEN_EXPIRATION_IN_MILLIS: Long = 3600000
}

object ProxyConstants {
    const val STOCKS_API_PATH = "/query"
    const val STOCKS_API_KEY = "ZGloeU1kSUNl" // This is a dummy key
    const val STOCKS_INTERVAL = "15min"
}
