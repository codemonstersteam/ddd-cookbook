package team.codemonsters.ddd.toolkit.result

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test

class FailTest {
    /**
     * тест помогает в знакомстве с основным строительным кирпичиком R.O.P
     */
    @Test
    fun failTestExceptionMessage() {
        val failTest: Result<String> = Result.failure(exception = RuntimeException("Some error"))
        Assertions.assertThat(failTest.isFailure).isTrue
        Assertions.assertThat(failTest.exceptionOrNull()?.message).isEqualTo("Some error")
    }
}