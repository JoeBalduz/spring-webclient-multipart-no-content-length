package com.example.mutlipartdatacontentlength

import org.springframework.core.io.ClassPathResource
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class UploadService {
    private val webClient = WebClient.builder().build()

    /**
     * Sends a multipart/form-data request. The request is either sent to the route that requires the content length or
     * not based on [sendToContentLengthRequiredRoute]
     */
    fun sendFile(sendToContentLengthRequiredRoute: Boolean): Mono<String> {
        val multipartBodyBuilder = MultipartBodyBuilder()
        multipartBodyBuilder.part("command", "This is a command")
        multipartBodyBuilder.part("myFile", ClassPathResource("test.txt").file.readBytes(), MediaType.APPLICATION_OCTET_STREAM)
            .header("Content-Disposition", "form-data; name=fileToUpload; filename=test.txt")


        val routeToSendTo = if (sendToContentLengthRequiredRoute) {
            "http://localhost:8080/placeToUpload"
        } else {
            "http://localhost:8080/placeToUploadWithoutContentLength"
        }

        return webClient.post()
            .uri(routeToSendTo)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .bodyValue(multipartBodyBuilder.build())
            .retrieve()
            .bodyToMono(String::class.java)
    }
}