// Code is adapted/inspired from https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/scores/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/scores/db/UserStatsService.kt
package no.pg6102exam.trip.db

import no.pg6102exam.trip.MOMListener
import org.slf4j.LoggerFactory
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.TypedQuery


@Repository
interface TripRepository : CrudRepository<Trip, Int> {
    fun existsByPlace(place: String): Boolean

}

@Service
@Transactional
class TripService(
        val repository: TripRepository,
        val em: EntityManager
) {

    companion object {
        private val log = LoggerFactory.getLogger(TripService::class.java)
    }


    @Transactional
    fun newTrip(place: String, duration: String, cost: Int): Trip? {

        var trip = Trip(place = place, duration = duration, cost = cost)

        trip = repository.save(trip)

        return trip
    }


    @Transactional
    fun updateTrip(tripId: Int, place: String, duration: String, cost: Int): Trip? {


        val exists = repository.existsById(tripId)
        var updatedTrip: Trip = Trip(place = "", duration = "", cost = 0)

        if (exists) {
            val trips = repository.findAll()
            for (trip in trips) {
                if (trip.tripId == tripId) {
                    updatedTrip = trip
                    updatedTrip.place = place
                    updatedTrip.duration = duration
                    updatedTrip.cost = cost
                    repository.save(updatedTrip)

                }
            }

        } else {
            log.info("This id is not found (${tripId})")
        }
        return updatedTrip
    }


    @Transactional
    fun patchTrip(tripId: Int, duration: String, cost: Int): Trip? {


        val exists = repository.existsById(tripId)
        var patchedTrip: Trip = Trip(duration = "", cost = 0)

        if (exists) {
            val trips = repository.findAll()
            for (trip in trips) {
                if (trip.tripId == tripId) {
                    patchedTrip = trip
                    patchedTrip.duration = duration
                    patchedTrip.cost = cost
                    repository.save(patchedTrip)

                }
            }

        } else {
            log.info("This id is not found (${tripId})")
        }
        return patchedTrip
    }


    @Transactional
    fun deleteTrip(id: Int): Boolean {

        return if (!repository.existsById(id)) {
            false

        } else {
            repository.deleteById(id)
            true
        }
    }


    fun getNextPage(size: Int, keysetId: Int? = null, keysetCost: Int? = null): List<Trip> {

        if (size < 1 || size > 1000) {
            throw IllegalArgumentException("Invalid size value: $size")
        }

        if ((keysetId == null && keysetCost != null) || (keysetId != null && keysetCost == null)) {
            throw IllegalArgumentException("keysetId and keysetCost should be both missing, or both present")
        }

        val query: TypedQuery<Trip>
        if (keysetId == null) {
            query = em.createQuery(
                    "select t from Trip t order by t.cost DESC, t.tripId DESC"
                    , Trip::class.java)
        } else {
            query = em.createQuery(
                    "select t from Trip t where t.cost<?2 or (t.cost=?2 and t.tripId<?1) order by t.cost DESC, t.tripId DESC"
                    , Trip::class.java)
            query.setParameter(1, keysetId)
            query.setParameter(2, keysetCost)
        }
        query.maxResults = size

        return query.resultList
    }
}