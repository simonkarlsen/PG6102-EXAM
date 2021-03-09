// Code is adapted/inspired from https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/user-collections/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/usercollections/dto/PatchUserDto.kt
package no.pg6102exam.trip.dto

import io.swagger.annotations.ApiModelProperty

// To be used in api (PATCH)
class PatchTripDto (

        @get:ApiModelProperty("The duration of the trip")
        var duration: String? = null,

        @get:ApiModelProperty("The trip cost per person")
        var cost: Int? = null
)
