package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import team.codemonsters.ddd.toolkit.controller.RestResponse
import team.codemonsters.ddd.toolkit.dto.SubscriberDto

@Component
class SubscriberGateway(val _restClient: SubscriberRestClient) {


    fun findDataUpdate(request: ValidatedDataUpdateRequest): Mono<Result<DataUpdate>> =
        unwrapCatching { _restClient.findDataUpdate(request.dataUpdateId.value) }.map { DataUpdate.emerge(it) }

    fun findSubscriber(subscriberId: SubscriberId): Mono<Result<Subscriber>> =
        unwrapCatching { _restClient.findSubscriber(subscriberId.value) }.map { forwardError(it) }

    private fun forwardError(subscriberDtoResult: Result<SubscriberDto>)
            : Result<Subscriber> =
        if (subscriberDtoResult.isSuccess)
            Subscriber.emerge(subscriberDtoResult.getOrThrow())
        else
            Result.failure(subscriberDtoResult.exceptionOrNull()!!)

    fun <R> unwrapCatching(block: () -> Mono<RestResponse<R>>): Mono<Result<R>> =
        try {
            block()
                .map { Result.success(it.valueOrThrow) }
        } catch (ex: Exception) {
            Mono.just(Result.failure(ex))
        }

}