package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

internal class SubscriberIdTest {
    @Test
    fun success() {
        val sut = SubscriberId.emerge("888")
        Assertions.assertThat(sut.isSuccess).isTrue
        Assertions.assertThat(sut.getOrThrow().value).isEqualTo("888")
    }

    @Test
    fun failWithMoreThen9Digits() {
        val sut = SubscriberId.emerge("1234567890")
        Assertions.assertThat(sut.isFailure).isTrue
        Assertions.assertThat(sut.exceptionOrNull()!!.message).isEqualTo("Subscriber Id consists of numbers maximum length 9")
    }

    @Test
    fun failWithWrongFormat() {
        val sut = SubscriberId.emerge("L124S")
        Assertions.assertThat(sut.isFailure).isTrue
        Assertions.assertThat(sut.exceptionOrNull()!!.message).isEqualTo("Subscriber Id consists of numbers maximum length 9")
    }
}