package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class ValidatedDataUpdateRequestTest {
    @Test
    fun success() {
        val sut = ValidatedDataUpdateRequest
            .emerge(UnvalidatedDataUpdateRequest("12345"))
        assertThat(sut.isSuccess).isTrue
        assertThat(sut.getOrThrow().dataUpdateId.value).isEqualTo("12345")
    }

}