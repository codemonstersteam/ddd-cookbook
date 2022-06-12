package team.codemonsters.ddd.toolkit.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate.SubscriberDataUpdateResponse

class SubscriberDataUpdateResponseDto private constructor(
    val subscriberId: String,
    val dataUpdateId: String
) {

    companion object {
        @JsonIgnore
        fun from(dataUpdateResult: SubscriberDataUpdateResponse): SubscriberDataUpdateResponseDto =
            SubscriberDataUpdateResponseDto(
                subscriberId = dataUpdateResult.subscriberId,
                dataUpdateId = dataUpdateResult.dataUpdateId
            )
    }

}
