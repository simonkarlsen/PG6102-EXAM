// Code is inspired/adapted from https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/scores/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/scores/db/FakeDataService.kt
package no.pg6102exam.trip.db

import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import javax.annotation.PostConstruct
import kotlin.random.Random


@Profile("FakeData")
@Service
@Transactional
class FakeDataService(
        val repository: TripRepository
) {



    @PostConstruct
    fun init(){
        for(i in 0..49){
            createRandomTrip(i)
        }
    }


    fun IntRange.random() = (Math.random() * ((endInclusive + 1) - start) + start).toInt()

    fun createRandomTrip(number: Int){
        val placeName = number + 1
        val randomTrip = Trip(
                place = "place$placeName",
                duration = "${(1..15).random()}/${(6..7).random()}/2021 " +
                        "to ${(15..30).random()}/${(7..8).random()}/2021",
                cost = Random.nextInt(1000))


        repository.save(randomTrip)
    }
}