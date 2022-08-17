package team.codemonsters.ddd.toolkit.controller

import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate.UnvalidatedDataUpdateRequest
import team.codemonsters.ddd.toolkit.domain.SubscriberDataUpdateResponseDto
import team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate.SubscriberDataUpdateResponse
import team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate.SubscriberDataUpdateService
import team.codemonsters.ddd.toolkit.dto.DataUpdateRequestDto

@RestController
@RequestMapping("/api/v1/")
class SubscriberDataUpdateController(val _subscriberDataUpdate: SubscriberDataUpdateService) {

    /**
     * Контпроллер очень прост в реализации:
     * получает запрос, трансформирует запрос и передает сервису бизнес-логики,
     * получает ответ в виде Result<success, failure> и трансформирует
     * в ответ по контракту REST API
     */
    @PutMapping("/subscriber-data-updates")
    fun subscriberDataUpdate(
        @RequestBody subscriberDataUpdate: RestRequest<DataUpdateRequestDto>
    ): Mono<RestResponse<SubscriberDataUpdateResponseDto>> =
        _subscriberDataUpdate.dataUpdateProcess(
            UnvalidatedDataUpdateRequest(subscriberDataUpdate.data.dataUpdateId)
        )
            .map { mapToDto(it) }
            .map { wrapToRestResponse(it) }

    private fun mapToDto(subscriberDataUpdateResult: Result<SubscriberDataUpdateResponse>)
            : Result<SubscriberDataUpdateResponseDto> =
        subscriberDataUpdateResult.map { SubscriberDataUpdateResponseDto.from(it) }

    private fun wrapToRestResponse(resultOfUpdate: Result<SubscriberDataUpdateResponseDto>) =
        when (resultOfUpdate.isSuccess) {
            true -> RestResponse.success(resultOfUpdate.getOrThrow())
            else -> RestResponse.fail(
                RestError(
                    errorMessage = resultOfUpdate.exceptionOrNull()!!.message!!,
                    code = "spell-error-code"
                )
            )
        }
}