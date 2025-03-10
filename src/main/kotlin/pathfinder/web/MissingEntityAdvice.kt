package pathfinder.web

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class MissingEntityAdvice: ResponseEntityExceptionHandler() {

    override fun handleMissingPathVariable(
        ex: MissingPathVariableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<in Any> = if (ex.message?.endsWith("is present but converted to null") == true)
        ResponseEntity.notFound().build()
    else
        super.handleMissingPathVariable(ex, headers, status, request)
}