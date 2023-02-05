package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate


import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import reactor.test.StepVerifier

@SpringBootTest
internal class SubscriberGatewayConnectionFailTest(
    @Autowired private val client: SubscriberGateway
) {

    @Test
    fun failCauseConnectionError() {
        val sut = client.findSubscriber(SubscriberId.emerge("123").getOrThrow())
        StepVerifier.create(sut)
            .assertNext{
                assertThat(it.isFailure).isTrue
                assertThat(it.exceptionOrNull()!!.message).contains("Connection refused: localhost/127.0.0.1:8080")
            }
            .verifyComplete()
    }

}