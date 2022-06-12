package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import team.codemonsters.ddd.toolkit.controller.RestResponse

@Component
class SubscriberGateway(val _restClient: SubscriberRestClient) {


    fun findDataUpdate(dataUpdateId: String): Mono<Result<DataUpdate>> =
        unwrapCatching { _restClient.findDataUpdate(dataUpdateId) }.map { DataUpdate.emerge(it)}

    fun findSubscriber(subscriberId: String): Mono<Result<Subscriber>> =
       unwrapCatching { _restClient.findSubscriber(subscriberId) }.map { Subscriber.emerge(it) }

    fun <R> unwrapCatching(block: () -> Mono<RestResponse<R>>): Mono<Result<R>> =
        try {
            block()
                .map { Result.success(it.valueOrThrow) }
        } catch (ex: Exception) {
            Mono.just(Result.failure(ex))
        }

}