package team.codemonsters.ddd.toolkit.domain

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.ActiveProfiles
import reactor.test.StepVerifier
import team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate.SubscriberGateway

@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 0)
@ActiveProfiles(profiles = ["test"])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SubscriberGatewayTest(
    @Autowired val gateway: SubscriberGateway
) {

    @Test
    fun findDataUpdate() {
        StepVerifier.create(gateway.findDataUpdate("101"))
            .assertNext {
                it.mapCatching {  }
                Assertions.assertThat(it.isSuccess).isTrue
                Assertions.assertThat(it.getOrThrow().dataUpdateId).isEqualTo("101")
                Assertions.assertThat(it.getOrThrow().subscriberId).isEqualTo("999")
                Assertions.assertThat(it.getOrThrow().mobileRegionId).isEqualTo("9")
                Assertions.assertThat(it.getOrThrow().msisdn).isEqualTo("9263336699")
            }
            .verifyComplete()
    }

}