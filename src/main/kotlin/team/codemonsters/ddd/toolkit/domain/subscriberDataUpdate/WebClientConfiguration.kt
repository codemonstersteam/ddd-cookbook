package team.codemonsters.ddd.toolkit.domain.subscriberDataUpdate

import io.netty.channel.ChannelOption
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ClientHttpConnector
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.util.concurrent.TimeUnit

@Configuration
class WebClientConfiguration(
    @Value("\${rest.services.subscribers.url}")
    private var host: String
) {
    @Bean
    fun webClientWithConnectAndReadTimeOuts(): WebClient {
        val connector: ClientHttpConnector = ReactorClientHttpConnector(
            HttpClient.create()
                .baseUrl(host)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 33000)
                .option(ChannelOption.SO_TIMEOUT, 33000)
                .doOnConnected {
                    it.addHandlerLast(ReadTimeoutHandler(20000, TimeUnit.MILLISECONDS))
                    it.addHandlerLast(WriteTimeoutHandler(20000, TimeUnit.MILLISECONDS))
                }
        )
        return WebClient.builder().clientConnector(connector).build()
    }
}
