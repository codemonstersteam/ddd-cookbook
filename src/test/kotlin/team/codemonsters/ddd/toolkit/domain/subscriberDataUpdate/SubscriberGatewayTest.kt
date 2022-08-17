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
internal class SubscriberGatewayTest(
    @Autowired val gateway: SubscriberGateway
) {

    @Test
    fun findDataUpdate() {
        val validRequest = ValidatedDataUpdateRequest
            .emerge(UnvalidatedDataUpdateRequest("101"))
            .getOrThrow()
        StepVerifier.create(gateway.findDataUpdate(validRequest))
            .assertNext {
                Assertions.assertThat(it.isSuccess).isTrue
                Assertions.assertThat(it.getOrThrow().dataUpdateId.value).isEqualTo("101")
                Assertions.assertThat(it.getOrThrow().subscriberId.value).isEqualTo("777")
                Assertions.assertThat(it.getOrThrow().mobileRegionId.value).isEqualTo("8")
                Assertions.assertThat(it.getOrThrow().msisdn.value).isEqualTo("8263336688")
            }
            .verifyComplete()
    }

}