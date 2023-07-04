package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import org.springframework.stereotype.Service
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
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
    fun dataUpdateProcess(unvalidatedUpdateRequest: UnvalidatedDataUpdateRequest)
            : Mono<Result<SubscriberDataUpdateResponse>> =
        Mono.just(ValidatedDataUpdateRequest.emerge(unvalidatedUpdateRequest))
            .pipe { findDataForUpdate(it) }
            .pipe { findSubscriber(it) }
            .mapResult { it.prepareUpdateRequest() }
            .pipe { updateSubscriber(it) }

    private fun findDataForUpdate(validRequest: ValidatedDataUpdateRequest) =
        _subscribersClient.findDataUpdate(validRequest)

    private fun findSubscriber(dataUpdate: DataUpdate)
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
