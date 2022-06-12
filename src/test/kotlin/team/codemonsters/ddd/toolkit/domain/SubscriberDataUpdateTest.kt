package team.codemonsters.ddd.toolkit.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate.DataUpdate
import team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate.Subscriber
import team.codemonsters.ddd.toolkit.dto.DataUpdateDto
import team.codemonsters.ddd.toolkit.dto.SubscriberDto

class SubscriberDataUpdateTest {

    @Test
    fun success() {
        //arrange
        val foundDataUpdateDto = DataUpdateDto(
            dataUpdateId = "101",
            subscriberId = "909",
            msisdn = "9999999999",
            mobileRegionId = "9" //<
        )
        val foundSubscriberDto = SubscriberDto(
            subscriberId = "909",
            msisdn = "9999999999",
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
        val subscriberUpdateRequest = sut.getOrThrow().prepareUpdateRequest().getOrThrow();
        assertThat(subscriberUpdateRequest.subscriberId).isEqualTo("909")
        assertThat(subscriberUpdateRequest.msisdn).isEqualTo("9999999999")
        assertThat(subscriberUpdateRequest.mobileRegionId).isEqualTo("9")
    }

    @Test
    fun failInSubscriber() {
        //arrange
        val dataUpdateDto = DataUpdateDto(
            dataUpdateId = "101",
            subscriberId = "909",
            msisdn = "9999999999",
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
            subscriberId = "909",
            msisdn = "9999999999",
            mobileRegionId = "9"
        )
        val dataUpdate = DataUpdate.emerge(Result.success(dataUpdateDto)).getOrThrow()
        val subscriberResult = Subscriber.emerge(
            Result.success(
                SubscriberDto(
                    subscriberId = "909",
                    msisdn = "9999999999",
                    mobileRegionId = "9"
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