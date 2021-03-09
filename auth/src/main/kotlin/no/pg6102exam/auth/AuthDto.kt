// From https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/auth/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/auth/AuthDto.kt
package no.pg6102exam.auth

import javax.validation.constraints.NotBlank


class AuthDto(
        @get:NotBlank
        var userId : String? = null,

        @get:NotBlank
        var password: String? = null
)