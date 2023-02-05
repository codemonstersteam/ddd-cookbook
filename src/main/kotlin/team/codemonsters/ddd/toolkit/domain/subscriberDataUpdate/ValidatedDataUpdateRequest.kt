package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate


data class ValidatedDataUpdateRequest
private constructor(val dataUpdateId: DataUpdateId) {

    companion object {
        fun emerge(unvalidatedRequest: UnvalidatedDataUpdateRequest)
                : Result<ValidatedDataUpdateRequest> =
            DataUpdateId.emerge(unvalidatedRequest.dataUpdateId)
                .map { ValidatedDataUpdateRequest(it) }

    }

}
