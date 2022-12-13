package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import team.codemonsters.ddd.toolkit.domain.common.zip
import team.codemonsters.ddd.toolkit.dto.SubscriberDto

data class Subscriber private constructor(
    val subscriberId: SubscriberId,
    val msisdn: Msisdn,
    val mobileRegionId: MobileRegionId
) {
    companion object {
        fun emerge(subscriberDto: SubscriberDto)
                : Result<Subscriber> =
            Result.zip(
                SubscriberId.emerge(subscriberDto.subscriberId),
                Msisdn.emerge(subscriberDto.msisdn),
                MobileRegionId.emerge(subscriberDto.mobileRegionId)
            ).map {
                Subscriber(
                    subscriberId = it.t1,
                    msisdn = it.t2,
                    mobileRegionId = it.t3
                )
            }

    }

}