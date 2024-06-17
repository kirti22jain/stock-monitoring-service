package stock.monitoring.service.interceptor

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.slf4j.LoggerFactory
import org.slf4j.MDC
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.web.servlet.AsyncHandlerInterceptor
import stock.monitoring.service.Default
import stock.monitoring.service.Default.AUTHORIZATION
import stock.monitoring.service.Default.HTTP_OPTIONS
import stock.monitoring.service.Default.PROTECTED_ENDPOINTS
import java.io.IOException
import javax.annotation.PostConstruct
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
@Profile("!it")
@Suppress("ReturnCount")
class HttpRequestInterceptor : AsyncHandlerInterceptor {

    private lateinit var protectedEndPoints: Set<String>
    private val logger = LoggerFactory.getLogger(HttpRequestInterceptor::class.java)
    private val pathMatcher = AntPathMatcher()

    @PostConstruct
    fun postConstruct() {
        protectedEndPoints = PROTECTED_ENDPOINTS
    }

    /**
     * Validates x-api-key passed in the request with the secret key stored in Vault
     */
    @Throws(IOException::class)
    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
    ): Boolean {
        if (!isPreflightRequest(request)) {
            val authorization = request.getHeader(AUTHORIZATION)
            return when {
                !isProtectedEndpoint(request) -> true
                isAuthorized(authorization) -> true
                else -> {
                    logger.error(
                        "Error=Not authorized to access url={}",
                        request.requestURI.toString(),
                    )
                    response.writer.write("{\"message\": \"Forbidden\"}")
                    response.contentType = APPLICATION_JSON.type
                    response.status = HttpServletResponse.SC_FORBIDDEN
                    false
                }
            }
        }
        return false
    }

    /**
     * Clears MDC after request is processed
     */
    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?,
    ) {
        MDC.clear()
    }

    private fun isAuthorized(token: String?): Boolean {
        if (token == null || !token.startsWith(Default.TOKEN_PREFIX)) {
            return false
        }
        val decodedJWT = JWT.require(Algorithm.HMAC512(Default.SHA_SECRET_KEY))
            .build()
            .verify(token.replace(Default.TOKEN_PREFIX, ""))
        val roles = decodedJWT.claims["roles"]?.asList(String::class.java)
        if (decodedJWT.subject != null && !roles.isNullOrEmpty()) {
            roles.forEach { role ->
                if (role in Default.ACCEPTED_ROLES) {
                    return true
                }
            }
        }
        return false
    }

    private fun isProtectedEndpoint(request: HttpServletRequest): Boolean {
        return PROTECTED_ENDPOINTS.any { pathMatcher.match(it, request.requestURI) }
    }

    private fun isPreflightRequest(req: HttpServletRequest): Boolean {
        return HTTP_OPTIONS.equals(req.method, ignoreCase = true)
    }
}
