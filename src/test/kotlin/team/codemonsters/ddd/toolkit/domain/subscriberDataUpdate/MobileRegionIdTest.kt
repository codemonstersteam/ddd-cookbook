package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class MobileRegionIdTest {
    @Test
    fun success() {
        val sut = MobileRegionId.emerge("1234")
        Assertions.assertThat(sut.isSuccess).isTrue
        Assertions.assertThat(sut.getOrThrow().value).isEqualTo("1234")
    }

    @Test
    fun failWithMoreThen4Digits() {
        val sut = MobileRegionId.emerge("12345")
        Assertions.assertThat(sut.isFailure).isTrue
        Assertions.assertThat(sut.exceptionOrNull()!!.message).isEqualTo("Mobile Region Id consists of numbers maximum length 4")
    }

    @Test
    fun failWithWrongFormat() {
        val sut = MobileRegionId.emerge("L124S")
        Assertions.assertThat(sut.isFailure).isTrue
        Assertions.assertThat(sut.exceptionOrNull()!!.message).isEqualTo("Mobile Region Id consists of numbers maximum length 4")
    }
}