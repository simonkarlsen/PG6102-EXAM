// Adapted from https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-11/api-gateway/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/apigateway/Application.kt
package no.pg6102exam.apigateway

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient


@EnableDiscoveryClient
@SpringBootApplication
class Application {
}


fun main(args: Array<String>) {
    SpringApplication.run(Application::class.java, *args)
}