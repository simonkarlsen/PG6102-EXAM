// (Adapted) from https://github.com/arcuri82/testing_security_development_enterprise_systems/blob/master/advanced/rest/rest-dto/src/main/kotlin/org/tsdes/advanced/rest/dto/RestResponseFactory.kt
package no.pg6102exam.rest.dto

import org.springframework.http.ResponseEntity
import java.net.URI

object RestResponseFactory {

    fun <T> notFound(message: String): ResponseEntity<WrappedResponse<T>> {

        return ResponseEntity.status(404).body(
                WrappedResponse<T>(code = 404, message = message)
                        .validated())
    }

    fun <T> userFailure(message: String, httpCode: Int = 400): ResponseEntity<WrappedResponse<T>> {

        return ResponseEntity.status(httpCode).body(
                WrappedResponse<T>(code = httpCode, message = message)
                        .validated())
    }

    fun <T> payload(httpCode: Int, data: T): ResponseEntity<WrappedResponse<T>> {

        return ResponseEntity.status(httpCode).body(
                WrappedResponse(code = httpCode, data = data)
                        .validated())
    }

    fun noPayload(httpCode: Int): ResponseEntity<WrappedResponse<Void>> {

        return ResponseEntity.status(httpCode).body(
                WrappedResponse<Void>(code = httpCode).validated())
    }

    fun created(uri: URI): ResponseEntity<WrappedResponse<Void>> {

        return ResponseEntity.created(uri).body(
                WrappedResponse<Void>(code = 201).validated())
    }

    fun <T> serverFailure(message: String, httpCode: Int = 500): ResponseEntity<WrappedResponse<T>> {

        return ResponseEntity.status(httpCode).body(
                WrappedResponse<T>(code = httpCode, message = message)
                        .validated())
    }
}