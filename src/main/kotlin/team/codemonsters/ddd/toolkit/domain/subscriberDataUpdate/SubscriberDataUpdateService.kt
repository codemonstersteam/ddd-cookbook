package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import team.codemonsters.ddd.toolkit.domain.SubscriberDataUpdate


@Service
class SubscriberDataUpdateService(val _subscribersClient: SubscriberGateway) {
    /**
     * Достоинства:
     *      просто прочитать и осознать
     *      является низкоуровневой документацией
     *      использует ROP - отсутствуют исключения как возможный неявный результат выполнения бизнес-логики
     *      бизнес логика отделена от координирующего слоя
     *      при таком подходе сам сервис выступает тупой трубой, который максимум использует паттерн canExecute/execute
     *      тестировать его не обязатально - можно покрыть минимум точек верхнеуровневым интеграционным тестом контроллера
     *      который будет покрывать максимум кода
     *      при тестировании можно сфокусироваться на тестировании сильной доменной модели - тестировании бизнес-логики
     */
    fun dataUpdateProcess(unvalidatedUpdateRequest: UnvalidatedDataUpdateRequest)
            : Mono<Result<SubscriberDataUpdateResponse>> =
        Mono.just(ValidatedDataUpdateRequest.emerge(unvalidatedUpdateRequest))
            .flatMap { findDataWithUpdates(it) }
            .flatMap { findSubscriberForUpdate(it) }
            .flatMap { prepareSubscriberUpdateRequest(it) }
            .flatMap { updateSubscriber(it) }

    fun findDataWithUpdates(validDataUpdateRequest: Result<ValidatedDataUpdateRequest>)
            : Mono<Result<DataUpdate>> =
        validDataUpdateRequest.fold(
            onSuccess = { success -> _subscribersClient.findDataUpdate(success) },
            //^canExecute/Execute
            onFailure = { error -> Mono.just(Result.failure(error)) }
            //^Проброс ошибки далее по пайпу
        )

    private fun findSubscriberForUpdate(dataUpdate: Result<DataUpdate>)
            : Mono<Result<SubscriberDataUpdate>> =
        dataUpdate.fold(
            onSuccess = { subscriberRequest -> findSubscriberByRest(subscriberRequest) },
            onFailure = { error -> Mono.just(Result.failure(error)) }
        )

    private fun findSubscriberByRest(dataUpdate: DataUpdate)
            : Mono<Result<SubscriberDataUpdate>> =
        _subscribersClient.findSubscriber(dataUpdate.subscriberId)
            .map { SubscriberDataUpdate.emerge(dataUpdate, it) }

    private fun prepareSubscriberUpdateRequest(subscriberDataUpdate: Result<SubscriberDataUpdate>)
            : Mono<Result<SubscriberUpdateRequest>> =
        subscriberDataUpdate.fold(
            onSuccess = { it.prepareUpdateRequest() },
            onFailure = { Result.failure(it) }
        ).toMono()

    private fun updateSubscriber(updateRequest: Result<SubscriberUpdateRequest>)
            : Mono<Result<SubscriberDataUpdateResponse>> =
        updateRequest.fold(
            onSuccess = { updateSubscriberByRest(it) },
            onFailure = { Mono.just(Result.failure(it)) }
        )

    private fun updateSubscriberByRest(subscriberDataUpdate: SubscriberUpdateRequest): Mono<Result<SubscriberDataUpdateResponse>> =
        Mono.just(
            Result.success(
                SubscriberDataUpdateResponse(
                    meta = "success",
                    dataUpdateId = subscriberDataUpdate.dataUpdateId,
                    subscriberId = subscriberDataUpdate.subscriberId
                )
            )
        )

}
