package com.example.mutlipartdatacontentlength

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class SendController(private val uploadService: UploadService) {

    /**
     * Calls the method to send a file. Can take a query parameter [sendToContentLengthRequiredRoute] which decides
     * whether to send the file to route where the content length is required or not. By default, it will try to send
     * the file to the route which requires that a content-length be sent in the header
     */
    @GetMapping("/sendFile")
    fun sendFile(@RequestParam(required = false) sendToContentLengthRequiredRoute: Boolean?): Mono<String> {
        return uploadService.sendFile(sendToContentLengthRequiredRoute ?: true)
    }
}