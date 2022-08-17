package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import reactor.util.function.Tuple4
import team.codemonsters.ddd.toolkit.domain.common.zip

data class DataUpdate private constructor(
    val dataUpdateId: DataUpdateId,
    val subscriberId: SubscriberId,
    val msisdn: Msisdn,
    val mobileRegionId: MobileRegionId
) {
    companion object {
        fun emerge(dataUpdateResult: Result<DataUpdateDto>)
                : Result<DataUpdate> =
            dataUpdateResult.fold(
                onFailure = { Result.failure(it) },
                onSuccess = {
                    wrapPrimitivesToValues(it)
                        .map { tuple4 -> tuple4ToDataUpdate(tuple4) }
                }
            )

        private fun wrapPrimitivesToValues(it: DataUpdateDto)
                : Result<Tuple4<DataUpdateId, SubscriberId, Msisdn, MobileRegionId>> =
            Result.zip(
                DataUpdateId.emerge(it.dataUpdateId),
                SubscriberId.emerge(it.subscriberId),
                Msisdn.emerge(it.msisdn),
                MobileRegionId.emerge(it.mobileRegionId)
            )
        private fun tuple4ToDataUpdate(tuple4: Tuple4<DataUpdateId, SubscriberId, Msisdn, MobileRegionId>) =
            DataUpdate(
                dataUpdateId = tuple4.t1,
                subscriberId = tuple4.t2,
                msisdn = tuple4.t3,
                mobileRegionId = tuple4.t4
            )
    }
}


