package stock.monitoring.service.configuration

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
    info = Info(
        title = "Stock Monitoring System",
        description = """This is used to handle different kinds of stock related operations.""",
        contact = Contact(
            name = "kirti22jain",
            email = "kirti22jain@gmail.com",
            url = "https://github.com/kirti22jain/stock-monitoring-service/",
        ),
    ),
    tags = [
        Tag(name = "Stocks"),
    ],
)
class OpenApiConfiguration
