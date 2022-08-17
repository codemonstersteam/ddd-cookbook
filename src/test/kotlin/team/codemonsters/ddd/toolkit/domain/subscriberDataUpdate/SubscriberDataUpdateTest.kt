package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import team.codemonsters.ddd.toolkit.domain.SubscriberDataUpdate
import team.codemonsters.ddd.toolkit.dto.SubscriberDto

internal class SubscriberDataUpdateTest {

    @Test
    fun success() {
        //arrange
        val foundDataUpdateDto = DataUpdateDto(
            dataUpdateId = "101",
            subscriberId = "888",
            msisdn = "3338887770",
            mobileRegionId = "9" //<
        )
        val foundSubscriberDto = SubscriberDto(
            subscriberId = "888",
            msisdn = "3338887770",
            mobileRegionId = "0" //<
        )

        val dataUpdate = DataUpdate.emerge(
            Result.success(foundDataUpdateDto)
        ).getOrThrow()
        val subscriberResult = Subscriber.emerge(
            Result.success(foundSubscriberDto)
        )
        //act
        val sut = SubscriberDataUpdate.emerge(dataUpdate, subscriberResult)
        //assert
        assertThat(sut.isSuccess).isTrue
        assertThat(sut.getOrThrow().prepareUpdateRequest().isSuccess).isTrue
        val subscriberUpdateRequest = sut.getOrThrow()
            .prepareUpdateRequest().getOrThrow()
        assertThat(subscriberUpdateRequest.subscriberId).isEqualTo("888")
        assertThat(subscriberUpdateRequest.msisdn).isEqualTo("3338887770")
        assertThat(subscriberUpdateRequest.mobileRegionId).isEqualTo("9")
    }

    @Test
    fun failInSubscriber() {
        //arrange
        val dataUpdateDto = DataUpdateDto(
            dataUpdateId = "101",
            subscriberId = "808",
            msisdn = "3338887770",
            mobileRegionId = "9"
        )
        val dataUpdate = DataUpdate.emerge(Result.success(dataUpdateDto)).getOrThrow()
        val subscriberResult = Result.failure<Subscriber>(RuntimeException("Subscriber not found"))
        //act
        val sut = SubscriberDataUpdate.emerge(dataUpdate, subscriberResult)
        //assert
        assertThat(sut.isFailure).isTrue
        assertThat(sut.exceptionOrNull()!!.message).isEqualTo("Subscriber not found")
    }

    @Test
    fun noUpdateRequiredFail() {
        //arrange
        val dataUpdateDto = DataUpdateDto(
            dataUpdateId = "101",
            subscriberId = "808",
            msisdn = "8888888888",
            mobileRegionId = "8"
        )
        val dataUpdate = DataUpdate.emerge(Result.success(dataUpdateDto)).getOrThrow()
        val subscriberResult = Subscriber.emerge(
            Result.success(
                SubscriberDto(
                    subscriberId = "808",
                    msisdn = "8888888888",
                    mobileRegionId = "8"
                )
            )
        )
        //act
        val sut = SubscriberDataUpdate.emerge(dataUpdate, subscriberResult)
        //assert
        assertThat(sut.isSuccess).isTrue
        assertThat(sut.getOrThrow().prepareUpdateRequest().isFailure).isTrue
    }

}