package com.example.mutlipartdatacontentlength

import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.stereotype.Service
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class UploadService {
    private val webClient = WebClient.builder().build()
    private val restTemplate = RestTemplate()

    /**
     * Sends a multipart/form-data request. The request is either sent to the route that requires the content length or
     * not based on [sendToContentLengthRequiredRoute]
     */
    fun sendFile(sendToContentLengthRequiredRoute: Boolean): Mono<String> {
        val routeToSendTo = if (sendToContentLengthRequiredRoute) {
            "http://localhost:8080/placeToUpload"
        } else {
            "http://localhost:8080/placeToUploadWithoutContentLength"
        }

        return webClient.post()
            .uri(routeToSendTo)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .bodyValue(createMultipartBody())
            .retrieve()
            .bodyToMono(String::class.java)
    }

    /**
     * Sends a file to a route which requires content-length by using RestTemplate
     */
    fun sendFileRestTemplate(): Mono<String> {
        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA
        val httpEntity = HttpEntity(createMultipartBody(), headers)

        val response = restTemplate.postForEntity("http://localhost:8080/placeToUpload", httpEntity, String::class.java)
        val responseBody = response.body

        return if (responseBody != null) {
            Mono.just(responseBody)
        } else {
            Mono.empty()
        }
    }

    /**
     * Creates the multipartbody for requests
     */
    private fun createMultipartBody(): MultiValueMap<String, HttpEntity<*>> {
        val multipartBodyBuilder = MultipartBodyBuilder()
        multipartBodyBuilder.part("command", "This is a command")
        multipartBodyBuilder.part("myFile", ClassPathResource("test.txt").file.readBytes(), MediaType.APPLICATION_OCTET_STREAM)
            .header("Content-Disposition", "form-data; name=fileToUpload; filename=test.txt")

        return multipartBodyBuilder.build()
    }
}
