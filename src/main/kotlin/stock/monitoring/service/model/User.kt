package stock.monitoring.service.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class User(
    @JsonIgnore
    val id: String? = null,

    @JsonProperty("userId")
    val userId: String? = UUID.randomUUID().toString(),

    @JsonProperty("firstName")
    val firstName: String,

    @JsonProperty("lastName")
    val lastName: String,

    @JsonProperty("email")
    val email: String,
)
