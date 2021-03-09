// Code is adapted/inspired from https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/scores/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/scores/dto/UserStatsDto.kt
package no.pg6102exam.trip.dto

import io.swagger.annotations.ApiModelProperty
import javax.persistence.Id
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

data class TripDto(

        @get:ApiModelProperty("The id of the trip")
        var tripId: Int? = null,

        @get:ApiModelProperty("The place details of the trip")
        var place: String? = null,

        @get:ApiModelProperty("The duration of the trip")
        var duration: String? = null,

        @get:ApiModelProperty("The trip cost per person")
        var cost: Int? = null

)

