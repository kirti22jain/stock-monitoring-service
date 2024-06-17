//package stock.monitoring.service.manager
//
//import org.springframework.http.HttpHeaders
//import java.util.*
//import java.util.function.Function
//
//class AuthenticationManager {
//
//    fun getHttpAuthenticationHeaders(authenticationConfig: AuthenticationConfig?): HttpHeaders? {
//        val decryptedAuthenticationConfig: AuthenticationConfig = decryptCredentials(authenticationConfig)
//        val httpHeaders = HttpHeaders()
//        Optional.ofNullable<Any>(decryptedAuthenticationConfig)
//                .map<Any>(AuthenticationConfig::getApiKey)
//                .map(Function<Any, U> { apiKey: Any? -> this.getApiKeyHttpHeader(apiKey) })
//                .ifPresent(httpHeaders::addAll)
//        Optional.ofNullable<Any>(decryptedAuthenticationConfig)
//                .map<Any>(AuthenticationConfig::getBasicAuth)
//                .map(Function<Any, U> { basicAuth: Any? -> this.getBasicAuthHttpHeader(basicAuth) })
//                .ifPresent(httpHeaders::addAll)
//        Optional.ofNullable<Any>(decryptedAuthenticationConfig)
//                .map<Any>(AuthenticationConfig::getOauth)
//                .map<HttpHeaders>(Function<Any, HttpHeaders> { oAuth: Any? -> getOAuthHttpHeader(oAuth, decryptedAuthenticationConfig.getId()) })
//                .ifPresent { values: HttpHeaders? -> httpHeaders.addAll(values!!) }
//        log.info("Added authentication headers with keys: {}", httpHeaders.keys)
//        return httpHeaders
//    }
//}