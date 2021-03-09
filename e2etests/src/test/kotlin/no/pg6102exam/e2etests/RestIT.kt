package no.pg6102exam.e2etests

import io.restassured.RestAssured
import io.restassured.RestAssured.given
import io.restassured.http.ContentType
import org.awaitility.Awaitility
import org.hamcrest.CoreMatchers.*
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.Matchers.greaterThan
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.testcontainers.containers.DockerComposeContainer
import org.testcontainers.containers.wait.strategy.Wait
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import java.io.File
import java.time.Duration
import java.util.concurrent.TimeUnit




@Disabled
@Testcontainers
class RestIT {
/*

    companion object {

        init {
            RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
            RestAssured.port = 80
        }

        class KDockerComposeContainer(id: String, path: File) : DockerComposeContainer<KDockerComposeContainer>(id, path)

        @Container
        @JvmField
        val env = KDockerComposeContainer("trip-app", File("../docker-compose.yml"))
                .withExposedService("discovery", 8500,
                        Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(240)))
                .withLogConsumer("trip_0") { print("[TRIP_0] " + it.utf8String) }
                .withLogConsumer("trip_1") { print("[TRIP_1] " + it.utf8String) }
                .withLocalCompose(true)


        @BeforeAll
        @JvmStatic
        fun waitForServers() {

            Awaitility.await().atMost(240, TimeUnit.SECONDS)
                    .pollDelay(Duration.ofSeconds(20))
                    .pollInterval(Duration.ofSeconds(10))
                    .ignoreExceptions()
                    .until {

                        given().baseUri("http://${env.getServiceHost("discovery", 8500)}")
                                .port(env.getServicePort("discovery", 8500))
                                .get("/v1/agent/services")
                                .then()
                                .body("size()", equalTo(5))

                        true
                    }
        }
    }

    @Test
    fun testGetTrips() {
        Awaitility.await().atMost(120, TimeUnit.SECONDS)
                .pollInterval(Duration.ofSeconds(10))
                .ignoreExceptions()
                .until {
                    given().get("/api/trips/v1_000")
                            .then()
                            .statusCode(200)
                            .body("data.list.size()", greaterThan(9))
                    true
                }
    }




*/
}

