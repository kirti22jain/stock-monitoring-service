package stock.monitoring.service

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableScheduling
class Application

fun main(vararg args: String) {
    val argsArray = Array(args.size) { args[it] }
    println("Starting application....")
    SpringApplication.run(arrayOf(Application::class.java), argsArray)
}
