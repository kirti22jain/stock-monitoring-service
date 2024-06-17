package stock.monitoring.service.exception

/**
 * Exception thrown when an api call fails.
 * @param statusCode the http status code.
 * @param reason error message.
 * Exceptions caused by api call failures.
 * - Do not require immediate attention.
 * - Likely not to have an immediate fix.
 */
class ProxyException(
    val reason: String,
    val statusCode: Int,
) : RuntimeException(reason) {
    override fun toString(): String {
        return "ProxyException(statusCode='$statusCode', reason='$reason')"
    }
}

/**
 * Custom status codes for the exceptions for easy identification via metrics.
 * DO NOT use the existing HTTP status codes.
 */
@Suppress("MagicNumber")
enum class CustomStatusCodes(val code: Int, val tag: String, val reasonPhrase: String) {
    // Server
    IMPROPER_API_RESPONSE(550, "IMPROPER_API_RESPONSE", "Improper API response"),
    RETRIES_EXHAUSTED(551, "RETRIES_EXHAUSTED", "Retries exhausted"),
    PUBLISHER_TIMEOUT(552, "REQUEST_TIMEOUT", "Request timed out"),
}
