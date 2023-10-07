package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import team.codemonsters.ddd.toolkit.domain.SubscriberDataUpdate
import team.codemonsters.ddd.toolkit.domain.common.mapResult
import team.codemonsters.ddd.toolkit.domain.common.pipe


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
    fun subscriberUpdate(
        unvalidatedUpdateRequest: UnvalidatedDataUpdateRequest
    ): Mono<Result<SubscriberDataUpdateResponse>> =
        validateRequest(unvalidatedUpdateRequest)
            .pipe { requestSubscriberUpdate(it) }
            .pipe { requestCurrentSubscriber(it) }
            .mapResult { it.prepareUpdateRequest() }
            .pipe { updateSubscriber(it) }

    private fun validateRequest(unvalidatedUpdateRequest: UnvalidatedDataUpdateRequest) =
        Mono.just(
            ValidatedDataUpdateRequest
                .emerge(unvalidatedUpdateRequest)
        )

    private fun requestSubscriberUpdate(validRequest: ValidatedDataUpdateRequest) =
        _subscribersClient.findDataUpdate(validRequest)

    private fun requestCurrentSubscriber(dataUpdate: DataUpdate)
            : Mono<Result<SubscriberDataUpdate>> =
        _subscribersClient.findSubscriber(dataUpdate.subscriberId)
            .map { SubscriberDataUpdate.emerge(dataUpdate, it) }

    private fun updateSubscriber(subscriberDataUpdate: SubscriberUpdateRequest): Mono<Result<SubscriberDataUpdateResponse>> =
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
