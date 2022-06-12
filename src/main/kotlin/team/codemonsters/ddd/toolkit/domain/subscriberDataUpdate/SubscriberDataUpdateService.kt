package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import team.codemonsters.ddd.toolkit.domain.SubscriberDataUpdate
import team.codemonsters.ddd.toolkit.domain.SubscriberDataUpdateRequest


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
    fun dataUpdateProcess(dataUpdateRequest: SubscriberDataUpdateRequest)
    : Mono<Result<SubscriberDataUpdateResponse>> =
        getDataUpdate(dataUpdateRequest)
            .flatMap { getSubscriber(it) }
            .flatMap { prepareSubscriberUpdateRequest(it) }
            .flatMap { updateSubscriber(it) }


    private fun getDataUpdate(dataUpdateRequest: SubscriberDataUpdateRequest): Mono<Result<DataUpdate>> =
        _subscribersClient.findDataUpdate(dataUpdateRequest.dataUpdateId)

    /**
     * используется паттерн canExecute/Execute
     */
    private fun getSubscriber(dataUpdate: Result<DataUpdate>): Mono<Result<SubscriberDataUpdate>> =
        Mono.just(dataUpdate)
            .filter { it.isSuccess }
            .flatMap { getSubscriber(it.getOrThrow()) }
            .switchIfEmpty(incomingFailInDataUpdate(dataUpdate))

    private fun getSubscriber(dataUpdate: DataUpdate): Mono<Result<SubscriberDataUpdate>> =
        _subscribersClient.findSubscriber(dataUpdate.subscriberId)
            .map {
                SubscriberDataUpdate.emerge(dataUpdate, it)
            }

    private fun incomingFailInDataUpdate(dataUpdate: Result<DataUpdate>): Mono<Result<SubscriberDataUpdate>> =
        Mono.just(Result.failure(dataUpdate.exceptionOrNull() ?: RuntimeException("Dumb error")))

    private fun prepareSubscriberUpdateRequest(subscriberDataUpdate: Result<SubscriberDataUpdate>): Mono<Result<SubscriberUpdateRequest>> =
        Mono.just(subscriberDataUpdate)
            .filter { it.isSuccess }
            .flatMap { prepareRequest(it.getOrThrow()) }
            .switchIfEmpty(incomingFailInSubscriberDataUpdate(subscriberDataUpdate))

    private fun prepareRequest(subscriberDataUpdate: SubscriberDataUpdate): Mono<Result<SubscriberUpdateRequest>> =
        Mono.just(subscriberDataUpdate.prepareUpdateRequest())
                                            //^ бизнес-логика в Доменном классе

    private fun incomingFailInSubscriberDataUpdate(subscriberDataUpdate: Result<SubscriberDataUpdate>): Mono<Result<SubscriberUpdateRequest>> =
        Mono.just(
            Result.failure(
                subscriberDataUpdate.exceptionOrNull() ?: RuntimeException("Dumb error failInSubscriberDataUpdate")
            )
        )
    /**
     * используется паттерн canExecute/Execute
     */
    private fun updateSubscriber(updateRequest: Result<SubscriberUpdateRequest>): Mono<Result<SubscriberDataUpdateResponse>> =
        Mono.just(updateRequest)
            .filter { it.isSuccess }
            .flatMap { updateSubscriberByRest(it.getOrThrow()) }
            .switchIfEmpty(incomingFailInSubscriberUpdateRequest(updateRequest))

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

    private fun incomingFailInSubscriberUpdateRequest(updateRequest: Result<SubscriberUpdateRequest>): Mono<Result<SubscriberDataUpdateResponse>> =
        Mono.just(Result.failure(updateRequest.exceptionOrNull() ?: RuntimeException("updateSubscriber")))


}
