// From https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/exercise-solutions/card-game/part-10/auth/src/main/kotlin/org/tsdes/advanced/exercises/cardgame/auth/db/User.kt
package no.pg6102exam.auth.db

import javax.persistence.*
import javax.persistence.JoinColumn
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name="USERS")
class User(

        @get:Id
        @get:NotBlank
        var username: String?,

        @get:NotBlank
        var password: String?,

        @get:ElementCollection
        @get:CollectionTable(name="authorities", joinColumns = [JoinColumn(name = "username")])
        @get:Column(name="authority")
        @get:NotNull
        var roles: Set<String>? = setOf(),

        @get:NotNull
        var enabled: Boolean? = true
)