package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import team.codemonsters.ddd.toolkit.controller.RestResponse
import team.codemonsters.ddd.toolkit.domain.common.flatMap
import team.codemonsters.ddd.toolkit.dto.SubscriberDto

@Component
class SubscriberGateway(val _restClient: SubscriberRestClient) {

    fun findDataUpdate(request: ValidatedDataUpdateRequest): Mono<Result<DataUpdate>> =
        _restClient.findDataUpdate(request.dataUpdateId.value)
            .flatMap { mapDataUpdateRestResultToDomain(it) }

    private fun mapDataUpdateRestResultToDomain(restResult: Result<RestResponse<DataUpdateDto>>): Mono<Result<DataUpdate>> =
        Mono.just(restResult)
            .filter { it.isSuccess }
            .map { unwrapRestResponse(it.getOrThrow()) }
            .map { mapDataUpdateDtoToDomain(it) }
            .switchIfEmpty(forwardErrorDataUpdate(restResult))

    private fun mapDataUpdateDtoToDomain(resultDto: Result<DataUpdateDto>) =
        resultDto.flatMap { DataUpdate.emerge(it) }

    private fun forwardErrorDataUpdate(restResult: Result<RestResponse<DataUpdateDto>>): Mono<Result<DataUpdate>> =
        Mono.just(
            Result.failure(
                restResult.exceptionOrNull() ?: RuntimeException("Dumb error failInSubscriberDataUpdate")
            )
        )

    fun findSubscriber(subscriberId: SubscriberId): Mono<Result<Subscriber>> =
        _restClient.findSubscriber(subscriberId.value)
            .flatMap { mapSubscriberRestResultToDomain(it) }

    private fun mapSubscriberRestResultToDomain(restResult: Result<RestResponse<SubscriberDto>>)
            : Mono<Result<Subscriber>> =
        Mono.just(restResult)
            .filter { it.isSuccess }
            .map { unwrapRestResponse(it.getOrThrow()) }
            .map { mapSubscriberDtoToDomain(it) }
            .switchIfEmpty(forwardErrorSubscriber(restResult))

    private fun mapSubscriberDtoToDomain(resultDto: Result<SubscriberDto>) =
        resultDto.flatMap { Subscriber.emerge(it) }

    private fun forwardErrorSubscriber(result: Result<RestResponse<SubscriberDto>>): Mono<Result<Subscriber>> =
        Mono.just(
            Result.failure(
                result.exceptionOrNull() ?: RuntimeException("Dumb error failInSubscriberDataUpdate")
            )
        )

    private fun <T> unwrapRestResponse(restResponse: RestResponse<T>): Result<T> =
        if (restResponse.isSuccess) {
            Result.success(restResponse.data!!)
        } else {
            Result.failure(RuntimeException(restResponse.error!!.errorMessage))
        }

}