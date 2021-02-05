# spring-webclient-multipart-no-content-length
A small demo to show that no content-length is being set when sending multipart/form-data using WebClient

A get request can be sent to http://localhost:8080/sendFile with an optional boolean query parameter called
sendToContentLengthRequiredRoute. If it is true, which is the default, it will send the file to a route
which requires a content length to be set. Since WebClient is not adding a content-length to the request,
it will always fail. If it is set to false, it will send the request to a route which does not require a
content-length.

A get request can also be sent to http://localhost:8080/sendFileWithRestTemplate to send the file using
RestTemplate nstead of WebClient. This is done to show the RestTemplate adds the content-length header
