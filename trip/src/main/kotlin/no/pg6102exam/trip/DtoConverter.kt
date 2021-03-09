// Code is adapted/inspired from https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/scores/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/scores/DtoConverter.kt
package no.pg6102exam.trip

import no.pg6102exam.trip.db.Trip
import no.pg6102exam.trip.dto.TripDto

object DtoConverter {

    fun transform(trip: Trip) : TripDto =
            trip.run { TripDto(tripId, place, duration, cost)}

    fun transform(trips: Iterable<Trip>) : List<TripDto> = trips.map { transform(it) }
}