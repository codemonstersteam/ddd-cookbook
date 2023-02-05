package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.ActiveProfiles
import reactor.test.StepVerifier

@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 0)
@ActiveProfiles(profiles = ["test"])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
internal class SubscriberGatewayFailJsonMappingTest(
    @Autowired val gateway: SubscriberGateway
) {

    @Test
    fun failCauseDataUpdateAbsent() {
        val validRequest = ValidatedDataUpdateRequest
            .emerge(UnvalidatedDataUpdateRequest("10111"))
            .getOrThrow()
        StepVerifier.create(gateway.findDataUpdate(validRequest))
            .assertNext {
                Assertions.assertThat(it.isFailure).isTrue
                Assertions.assertThat(it.exceptionOrNull()!!.message).contains("JSON decoding error: Instantiation of ")
            }
            .verifyComplete()
    }

}