package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DataUpdateTest {

    @Test
    fun success() {
        val updateDto = DataUpdateDto(
            dataUpdateId = "101",
            subscriberId = "777",
            msisdn = "8263336688",
            mobileRegionId = "8"
        )
        val sut = DataUpdate.emerge(updateDto)
        assertThat(sut.isSuccess).isTrue
        assertThat(sut.getOrThrow().dataUpdateId.value).isEqualTo("101")
        assertThat(sut.getOrThrow().subscriberId.value).isEqualTo("777")
        assertThat(sut.getOrThrow().msisdn.value).isEqualTo("8263336688")
        assertThat(sut.getOrThrow().mobileRegionId.value).isEqualTo("8")
    }

}