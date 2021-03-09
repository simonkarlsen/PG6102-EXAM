// Code is adapted/inspired from https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/scores/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/scores/MOMListener.kt
// Not used
package no.pg6102exam.trip

import no.pg6102exam.trip.db.TripService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Service

@Service
class MOMListener(
        private val statsService: TripService
) {

    companion object{
        private val log = LoggerFactory.getLogger(MOMListener::class.java)
    }
//
//    @RabbitListener(queues = ["#{queue.name}"])
//    fun receiveFromAMQP(userId: String) {
////        val ok = statsService.registerNewTrip(userId)
////        if(ok){
//            log.info("via MOM: $userId")
////        }
//    }
}