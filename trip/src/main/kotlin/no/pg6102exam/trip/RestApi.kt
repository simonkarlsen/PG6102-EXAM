// Code is adapted/inspired from https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/scores/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/scores/RestApi.kt
// and https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-08/cards/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/cards/RestApi.kt
package no.pg6102exam.trip

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import no.pg6102exam.rest.dto.PageDto
import no.pg6102exam.rest.dto.RestResponseFactory
import no.pg6102exam.rest.dto.WrappedResponse
import no.pg6102exam.trip.db.TripRepository
import no.pg6102exam.trip.db.TripService
import no.pg6102exam.trip.dto.PatchTripDto
import no.pg6102exam.trip.dto.TripDto
import org.springframework.http.CacheControl
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.concurrent.TimeUnit


@Api(value = "/api/trips", description = "Trips sorted on cost and id")
@RequestMapping(
        path = ["/api/trips"],
        produces = [(MediaType.APPLICATION_JSON_VALUE)]
)
@RestController
class RestApi(private val tripRepository: TripRepository,
              private val tripService: TripService) {

    companion object{
        const val LATEST = "v1_000"
    }

    @ApiOperation("Old-version endpoints. Will automatically redirect to most recent version")
    @GetMapping(path = [
        "/v0_001",
        "/v0_002",
        "/v0_003"
    ])
    fun getOld() : ResponseEntity<Void>{

        return ResponseEntity.status(301)
                .location(URI.create("/api/trips/$LATEST"))
                .build()
    }

    @ApiOperation("Will automatically redirect to most recent version from base path")
    @GetMapping
    fun redirectFromBase() : ResponseEntity<Void>{

        return ResponseEntity.status(301)
                .location(URI.create("/api/trips/$LATEST"))
                .build()
    }


    @ApiOperation("Retrieve a given trip by id")
    @GetMapping(path = ["/$LATEST/{id}"])
    fun getTrip(
            @PathVariable("id") id: Int
    ): ResponseEntity<WrappedResponse<TripDto>> {

        val trip = tripRepository.findById(id).orElse(null)
                ?: return RestResponseFactory.notFound("Trip with id $id not found")
        return RestResponseFactory.payload(200, DtoConverter.transform(trip))
    }

    @ApiOperation("Return an iterable page of trips, " +
            "starting from the highest cost and newest id (newest added trip)")
    @GetMapping("/$LATEST")
    fun getAllLatest(
            @ApiParam("Id of trip in the previous page")
            @RequestParam("keysetId", required = false)
            keysetId: Int?,
            //
            @ApiParam("Cost of the trip in the previous page")
            @RequestParam("keysetCost", required = false)
            keysetCost: Int?): ResponseEntity<WrappedResponse<PageDto<TripDto>>> {

        val page = PageDto<TripDto>()

        val n = 10
        val trips = DtoConverter.transform(tripService.getNextPage(n, keysetId, keysetCost))
        page.list = trips

        if (trips.size == n) {
            val last = trips.last()
            page.next = "/api/trips/$LATEST?keysetId=${last.tripId}&keysetCost=${last.cost}"
        }

        return ResponseEntity
                .status(200)
                .cacheControl(CacheControl.maxAge(1, TimeUnit.MINUTES).cachePublic())
                .body(WrappedResponse(200, page).validated())
    }

    @ApiOperation("Create new Trip")
    @PostMapping
    fun createTrip(
            @ApiParam("Data for the new Trip")
            @RequestBody dto: TripDto
    ): ResponseEntity<WrappedResponse<Void>> {

        // Check if place exists
        if (tripRepository.existsByPlace(dto.place!!))
            return RestResponseFactory.userFailure(
                    "This Trip place {${dto.place}} already exists"
            )

        val trip = tripService.newTrip(
                dto.place!!,
                dto.duration!!,
                dto.cost!!
        ) ?: return RestResponseFactory.serverFailure(
                "Failed creating new trip",
                500)

        // path to the new trip
        return RestResponseFactory.created(URI.create("/api/trips/${trip.tripId}"))
    }

    @ApiOperation("Delete a specific Trip by id")
    @DeleteMapping("/{id}")
    fun deleteTripById(
            @ApiParam("The id of the trip")
            @PathVariable("id") id: String
    ): ResponseEntity<WrappedResponse<Void>> {

        // Converting to Int
        val tripId = id.toIntOrNull()
                ?: return RestResponseFactory.userFailure("The trip id must be a number")

        if (!tripRepository.existsById(tripId))
            return RestResponseFactory.userFailure("This trip does not exist (id: ${tripId})")

        tripRepository.deleteById(tripId)
        // 204 success (no content)
        return RestResponseFactory.noPayload(204)
    }

    @ApiOperation("Change a Trip")
    @PutMapping("/{id}")
    fun changeTrip(
            @ApiParam("Data for the new Trip")
            @PathVariable("id") id: String,
            @RequestBody dto: TripDto
    ): ResponseEntity<WrappedResponse<Void>> {

        val tripId = id.toIntOrNull()
                ?: return RestResponseFactory.userFailure(
                        "This Trip id is null")

        // Check if place exists
        if (tripRepository.existsByPlace(dto.place!!))
            return RestResponseFactory.userFailure(
                    "This Trip place {${dto.place}} already exists")
        else
            if ((tripService.updateTrip(tripId, dto.place!!, dto.duration!!, dto.cost!!) == null))
                return RestResponseFactory.serverFailure(
                        "Failed updating trip",
                        500)

        return RestResponseFactory.noPayload(201)
    }


    @ApiOperation("Change some values (duration and cost) in a specific trip")
    @PatchMapping(
            path = ["/{id}"],
            consumes = [(MediaType.APPLICATION_JSON_VALUE)]
    )
    fun patchUser(
            @PathVariable("id") id: String,
            @RequestBody dto: PatchTripDto
    ): ResponseEntity<WrappedResponse<Void>> {

        val tripId = id.toIntOrNull()
                ?: return RestResponseFactory.userFailure(
                        "This Trip id is null")

        if ((tripService.patchTrip(tripId, dto.duration!!, dto.cost!!) == null))
            return RestResponseFactory.serverFailure(
                    "Failed updating trip",
                    500)

        return RestResponseFactory.noPayload(201)
    }

}


