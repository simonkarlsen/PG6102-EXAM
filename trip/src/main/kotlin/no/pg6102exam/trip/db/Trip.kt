// Code is adapted/inspired from https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/scores/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/scores/db/UserStats.kt
package no.pg6102exam.trip.db

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


@Entity
class Trip(


        @get:Id
        @get:GeneratedValue(strategy = GenerationType.SEQUENCE)
        var tripId: Int? = 0,

        @get:NotBlank
        var place: String? = null,

        @get:NotBlank
        var duration: String? = null,

        @get:Min(0) @get:NotNull
        var cost: Int = 0

)
