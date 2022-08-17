package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class DataUpdateIdTest {

    @Test
    fun success() {
        val sut = DataUpdateId.emerge("12345")
        assertThat(sut.isSuccess).isTrue
        assertThat(sut.getOrThrow().value).isEqualTo("12345")
    }

    @Test
    fun failWithMoreThen9Digits() {
        val sut = DataUpdateId.emerge("1234567890")
        Assertions.assertThat(sut.isFailure).isTrue
        Assertions.assertThat(sut.exceptionOrNull()!!.message).isEqualTo("Data update Id consists of numbers maximum length 9")
    }

    @Test
    fun failWithWrongFormat() {
        val sut = DataUpdateId.emerge("L124S")
        Assertions.assertThat(sut.isFailure).isTrue
        Assertions.assertThat(sut.exceptionOrNull()!!.message).isEqualTo("Data update Id consists of numbers maximum length 9")
    }
}