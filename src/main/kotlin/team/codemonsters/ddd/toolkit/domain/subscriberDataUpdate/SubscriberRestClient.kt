package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import team.codemonsters.ddd.toolkit.dto.SubscriberDto
import team.codemonsters.ddd.toolkit.dto.DataUpdateDto
import team.codemonsters.ddd.toolkit.controller.RestResponse

@Component
class SubscriberRestClient(
    val webClient: WebClient,
    @Value("\${rest.services.subscribers.updates}")
    private var dataUpdatesUrl: String,
    @Value("\${rest.services.subscribers.subscribers}")
    private var subscribersUrl: String,
) {

    fun findDataUpdate(dataUpdateId: String): Mono<RestResponse<DataUpdateDto>> {
        val typeRef: ParameterizedTypeReference<RestResponse<DataUpdateDto>> =
            object : ParameterizedTypeReference<RestResponse<DataUpdateDto>>() {}
        return webClient
            .get()
            .uri(dataUpdatesUrl, dataUpdateId)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(typeRef)
    }

    fun findSubscriber(subscriberId: String): Mono<RestResponse<SubscriberDto>> {
        val typeRef: ParameterizedTypeReference<RestResponse<SubscriberDto>> =
            object : ParameterizedTypeReference<RestResponse<SubscriberDto>>() {}
        return webClient
            .get()
            .uri(subscribersUrl, subscriberId)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(typeRef)
    }


//    fun findSubscriber(targetName: String): Mono<RestResponse<SubscriberDto>> {
//        val typeRef: ParameterizedTypeReference<RestResponse<SubscriberDto>> =
//            object : ParameterizedTypeReference<RestResponse<SubscriberDto>>() {}
//        return webClient
//            .put()
//            .uri(subscribersUrl, targetName)
//            .accept(MediaType.APPLICATION_JSON)
//            .retrieve()
//            .bodyToMono(typeRef)
//    }
}