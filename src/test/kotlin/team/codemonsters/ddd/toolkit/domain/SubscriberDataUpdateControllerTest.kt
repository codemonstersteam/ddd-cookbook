package team.codemonsters.ddd.toolkit.domain

import org.junit.jupiter.api.Test
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.EntityExchangeResult
import org.springframework.test.web.reactive.server.WebTestClient
import team.codemonsters.ddd.toolkit.dto.DataUpdateRequestDto
import team.codemonsters.ddd.toolkit.controller.RestRequest

@AutoConfigureWebTestClient
@AutoConfigureWireMock(port = 0)
@ActiveProfiles(profiles = ["test"])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SubscriberDataUpdateControllerTest(@Autowired val webTestClient: WebTestClient) {
    val log: Logger = LoggerFactory.getLogger(SubscriberDataUpdateControllerTest::class.java)

    /**
     * Достоинства:
     *      легко понять
     *      покрывает максимум исполняемого кода бизнес-логики
     *      Очень хорошо показывает себя на этапе MVP.
     *      При росте проекта требуется ограничение:
     *          минимальное количество таких тестов допустимо, но не более того,
     *          тестируем «Счастливый путь» и выборочно точки отказа,
     *          чтобы убедиться в корректной работе «Ошибочного пути»
     * Недостатки:
     *      требуется настройка заглушек для всех интеграций, которые вызываются при исполнении бизнес-логики
     *
     * Чтобы оценить недостатки теста:
     * Рассмотрим тест по 4-м аспектам хороших юнит-тестов и оценим от 0-5
     *    1) защита от багов - 5
     *    2) устойчивость к рефакторингу - 5
     *    3) быстрая обратная связь - 2
     *          поднимается весь тестовый контекст (требует больше ресурсов время, проц, память)
     *          стартует сервер wireMock
     *          необходима дополнительная экспертиза в конфигурации wireMock
     *    4) простота поддержки - 2
     *          необходима дополнительная экспертиза в конфигурации wireMock
     *          при росте количества таких тестов сложность поддержки и конфигурации возрастает
     *          с ростом конфигурационных файлов, соглашений как называть файлы и как сверять запросы.

     */
    @Test
    fun updateSuccess() {
        webTestClient.put()
            .uri("/api/v1/subscriber-data-updates")
            .bodyValue(RestRequest(DataUpdateRequestDto(dataUpdateId = "101")))
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .consumeWith { bytes: EntityExchangeResult<ByteArray> ->
                log.info(
                    "RestResponse: {}",
                    String(bytes.responseBody!!)
                )
            }
            .jsonPath("@.actualTimestamp").isNotEmpty
            .jsonPath("@.status").isEqualTo("success")
            .jsonPath("@.data.subscriberId").isEqualTo("999")
            .jsonPath("@.data.dataUpdateId").isEqualTo("101")
    }

}