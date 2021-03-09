// Code is adapted/inspired from https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/scores/src/test/kotlin/org/tsdes/advanced/exercises/cardgame/scores/db/UserStatsServiceTest.kt
package no.pg6102exam.trip

import io.restassured.RestAssured
import io.restassured.common.mapper.TypeRef
import io.restassured.http.ContentType
import no.pg6102exam.rest.dto.PageDto
import no.pg6102exam.trip.db.FakeDataService
import no.pg6102exam.trip.db.Trip
import no.pg6102exam.trip.db.TripRepository
import no.pg6102exam.trip.db.TripService
import no.pg6102exam.trip.dto.PatchTripDto
import no.pg6102exam.trip.dto.TripDto
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.*
import org.hamcrest.Matchers.greaterThan
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.event.annotation.AfterTestClass
import org.springframework.test.context.junit.jupiter.SpringExtension
import javax.annotation.PostConstruct
import kotlin.random.Random
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType


@ActiveProfiles("FakeData,test")
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [(Application::class)],
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class RestApiTest{

    @LocalServerPort
    protected var port = 0

    @Autowired
    private lateinit var repository: TripRepository

    @Autowired
    private lateinit var service: TripService

//    @BeforeEach
//    @AfterEach
    @PostConstruct
    fun init(){
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = port
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()

//        repository.deleteAll()
    }



    val page : Int = 10


    companion object {
        private val log = LoggerFactory.getLogger(RestApiTest::class.java)

        const val LATEST = "v1_000"

        private var counter = 1

        private fun getRandomId(): Int {
            return counter++
        }


        fun getRandomDto(): TripDto {
            val id = getRandomId()
            val place = "place_test_$id"
            val duration = "duration_test_$id"

            return TripDto(place = place, duration = duration, cost = 200 + id)
        }

    }


    fun getFakeData(end: Int) {
        for(i in 0..end){
            val placeName =  getRandomId()
            val randomTrip = Trip(
                    place = "place$placeName",
                    duration = "${(1..15).random()}/${(7..8).random()}/2020 " +
                            "to ${(15..30).random()}/${(8..9).random()}/2020",
                    cost = Random.nextInt(1000))


            repository.save(randomTrip)
        }
    }


    @Test
    fun testInit(){
        // create fakeData
//        getFakeData(10)
        assertTrue(repository.count() > 10)
    }


    @Test
    fun testGetPage() {
        // create fakeData to test pages
//        getFakeData(10)

        RestAssured.given().accept(ContentType.JSON)
                .get("/api/trips/$LATEST")
                .then()
                .statusCode(200)
                .body("data.list.size()", CoreMatchers.equalTo(page))
    }

    @Test
    fun testPage(){

//        getFakeData(10)

        val n = 5
        val page = service.getNextPage(n)
        assertEquals(n, page.size)

        for(i in 0 until n-1){
            assertTrue(page[i].cost >= page[i+1].cost)
        }
    }


    @Test
    fun testGetAllPages(){

        // create fakeData to test pages
//        getFakeData(49)

        val read = mutableSetOf<String>()

        var page = RestAssured.given().accept(ContentType.JSON)
                .get("/api/trips/$LATEST")
                .then()
                .statusCode(200)
                .body("data.list.size()", CoreMatchers.equalTo(page))
                .extract().body().jsonPath().getObject("data",object: TypeRef<PageDto<Map<String, Any>>>(){})
        read.addAll(page.list.map { it["tripId"].toString()})

        checkOrder(page)

        while(page.next != null){
            page = RestAssured.given().accept(ContentType.JSON)
                    .get(page.next)
                    .then()
                    .statusCode(200)
                    .extract().body().jsonPath().getObject("data",object: TypeRef<PageDto<Map<String, Any>>>(){})
            read.addAll(page.list.map { it["tripId"].toString()})
            checkOrder(page)
        }

        val total = repository.count().toInt()

        //recall that sets have unique elements
        assertEquals(total, read.size)


    }

    private fun checkOrder(page: PageDto<Map<String, Any>>) {
        for (i in 0 until page.list.size - 1) {
            val acost = page.list[i]["cost"].toString().toInt()
            val bcost = page.list[i + 1]["cost"].toString().toInt()
            val aid = page.list[i]["tripId"].toString()
            val bid = page.list[i + 1]["tripId"].toString()
            assertTrue(acost >= bcost)
            log.info("acost: $acost, bcost: $bcost")
            if (acost == bcost) {
                log.info("aid: $aid, bid: $bid")
                // kind of flaky. Can be uncommented an tested, if you want to.
//                assertTrue(aid > bid)
            }
        }
    }


    @Test
    fun testOldVersion1() {
//        getFakeData(10)

        RestAssured.given().get("/api/trips/v0_001")
                .then()
                .statusCode(200)
                .body("data.list.size", equalTo(10))
    }

    @Test
    fun testOldVersion2() {
//        getFakeData(10)

        RestAssured.given().get("/api/trips/v0_002")
                .then()
                .statusCode(200)
                .body("data.list.size", equalTo(10))
    }

    @Test
    fun testOldVersion3() {
//        getFakeData(10)

        RestAssured.given().get("/api/trips/v0_003")
                .then()
                .statusCode(200)
                .body("data.list.size", equalTo(10))
    }

    @Test
    fun redirectFromBasePath() {
//        getFakeData(10)

        RestAssured.given().get("/api/trips")
                .then()
                .statusCode(200)
                .body("data.list.size", equalTo(10))
    }


    @Test
    fun testNewTrip() {

        val dto = getRandomDto()
        val id = newTrip(dto)
        val trip = repository.findById(id)

        assertTrue(trip.isPresent)
    }

    fun newTrip(dto: TripDto): Int{
        // POSTing a new trip. Should get id from header
        val test =
                RestAssured.given()
                        .contentType(ContentType.JSON)
                        .body(dto)
                        .post("/api/trips/")
                        .then().assertThat()
                        .statusCode(201)
                        .log().all()
                        .extract()
                        .header("Location")

        // Returns the extracted id from the header (location)
        return test.substringAfter("/api/trips/").toInt()
    }



    @Test
    fun deleteById() {

        val dto = getRandomDto()
        val id = newTrip(dto)

        RestAssured.given()
                .accept(ContentType.JSON)
                .delete("/api/trips/$id")
                .then().assertThat()
                .statusCode(204)
                .log().all()

        RestAssured.given()
                .accept(ContentType.JSON)
                .get("/api/trips/$LATEST/$id")
                .then().assertThat()
                .statusCode(404)
                .log().all()

        assertFalse(repository.existsById(id))
    }

    // PUT
    @Test
    fun updateTripById() {
        val dto = getRandomDto()
        // POST new trip
        val id = newTrip(dto)

        val updatedDto = TripDto(
                place = "updatedPlaceTest",
                duration = "updatedDurationTest",
                cost = 1234
        )

        // Updating to updatedDto
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(updatedDto)
                .put("/api/trips/$id")
                .then().assertThat()
                .statusCode(201)
                .log().all()

        // make sure that the trip is updated
        RestAssured.given()
                .accept(ContentType.JSON)
                .get("/api/trips/$LATEST/$id")
                .then().assertThat()
                .statusCode(200)
                .log().all()
                .body("data.tripId", equalTo(id))
                .body("data.place", equalTo(updatedDto.place))
                .body("data.duration", equalTo(updatedDto.duration))
                .body("data.cost", equalTo(updatedDto.cost))
                .log().all()

        assertTrue(repository.existsByPlace("updatedPlaceTest"))

    }

    @Test
    fun patchTripById() {
        val dto = getRandomDto()
        // POST new trip
        val id = newTrip(dto)

        val patchedDto = PatchTripDto(
                duration = "patchedDurationTest",
                cost = 300
        )

        // Patching patchedDto into trip
        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(patchedDto)
                .patch("/api/trips/$id")
                .then().assertThat()
                .statusCode(201)
                .log().all()

        // make sure that the trip is patched
        RestAssured.given()
                .accept(ContentType.JSON)
                .get("/api/trips/$LATEST/$id")
                .then().assertThat()
                .statusCode(200)
                .log().all()
                .body("data.tripId", equalTo(id))
                .body("data.duration", equalTo(patchedDto.duration))
                .body("data.cost", equalTo(patchedDto.cost))
                .log().all()

//        assertTrue(repository.existsById(id))
        val trip = repository.findById(id)
        assertEquals(patchedDto.duration, trip.get().duration)
        assertEquals(patchedDto.cost, trip.get().cost)

    }

}