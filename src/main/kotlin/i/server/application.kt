package i.server

import org.d7z.logger4k.core.utils.getLogger
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import java.util.UUID

@EnableWebMvc
@ConfigurationPropertiesScan
@SpringBootApplication
class ServerApplication

private val logger = ServerApplication::class.getLogger()

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
    System.setProperty("project.session", UUID.randomUUID().toString())
    logger.debug("当前会话代号：{}.", System.getProperty("project.session"))
}
