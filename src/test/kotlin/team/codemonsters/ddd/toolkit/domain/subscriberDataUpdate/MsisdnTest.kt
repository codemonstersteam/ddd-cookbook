package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class MsisdnTest {
    @Test
    fun success() {
        val sut = Msisdn.emerge("1234567890")
        Assertions.assertThat(sut.isSuccess).isTrue
        Assertions.assertThat(sut.getOrThrow().value).isEqualTo("1234567890")
    }

    @Test
    fun failWithMoreThen10Digits() {
        val sut = Msisdn.emerge("12345678901")
        Assertions.assertThat(sut.isFailure).isTrue
        Assertions.assertThat(sut.exceptionOrNull()!!.message)
            .isEqualTo("Msisdn consists of 10 numbers")
    }

    @Test
    fun failWithLessThen10Digits() {
        val sut = Msisdn.emerge("123456789")
        Assertions.assertThat(sut.isFailure).isTrue
        Assertions.assertThat(sut.exceptionOrNull()!!.message)
            .isEqualTo("Msisdn consists of 10 numbers")
    }

    @Test
    fun failWithWrongFormat() {
        val sut = Msisdn.emerge("L124S")
        Assertions.assertThat(sut.isFailure).isTrue
        Assertions.assertThat(sut.exceptionOrNull()!!.message)
            .isEqualTo("Msisdn consists of 10 numbers")
    }

}