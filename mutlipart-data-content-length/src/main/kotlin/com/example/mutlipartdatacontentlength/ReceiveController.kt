package com.example.mutlipartdatacontentlength

import org.springframework.http.codec.multipart.FilePart
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class ReceiveController {

    /**
     * Receives a a file along with a command. Requires that a content length be set
     */
    @PostMapping("/placeToUpload")
    fun receiveFile(
        @RequestHeader("Content-Length", required = true) contentLength: Long,
        @RequestPart command: String,
        @RequestPart myFile: Mono<FilePart>
    ): Mono<String> {
        return Mono.just("Received file with content-length as $contentLength")
    }

    /**
     * Receives a file along with a command
     */
    @PostMapping("/placeToUploadWithoutContentLength")
    fun receiveFileWithoutContentLength(@RequestPart command: String, @RequestPart myFile: Mono<FilePart>): Mono<String> {
        return Mono.just("Received file without content length")
    }
}
