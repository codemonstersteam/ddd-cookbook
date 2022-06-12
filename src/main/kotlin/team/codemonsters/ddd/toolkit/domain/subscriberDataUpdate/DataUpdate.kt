package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import team.codemonsters.ddd.toolkit.dto.DataUpdateDto

data class DataUpdate private constructor(
    val dataUpdateId: String,
    val subscriberId: String,
    val msisdn: String,
    val mobileRegionId: String
) {
    companion object {
        fun emerge(dataUpdateResult: Result<DataUpdateDto>): Result<DataUpdate> =
            dataUpdateResult.map { DataUpdate(it.dataUpdateId, it.subscriberId, it.msisdn, it.mobileRegionId) }
    }
}


