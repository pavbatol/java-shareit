package ru.practicum.shareit.user;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.user.dto.UserDtoAdd;
import ru.practicum.shareit.user.dto.UserDtoUpdate;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class UserClient extends BaseClient {
    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit-server.url}") String serverUrl,
                      @Value("${connect.read_timeout_millis:5000}") int readTimeout,
                      @Value("${connect.write_timeout_millis:5000}") int writeTimeout,
                      @Value("${connect.connect_timeout_millis:5000}") int connectTimeout,
                      @Value("${connect.response_timeout_millis:5000}") int responseTimeout,
                      WebClient.Builder builder) {

        super(builder
                .baseUrl(serverUrl + API_PREFIX)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.setAccept(List.of(MediaType.APPLICATION_JSON));
                    httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                })
                .clientConnector(new ReactorClientHttpConnector(
                        HttpClient.create()
                                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                                .responseTimeout(Duration.ofMillis(responseTimeout))
                                .doOnConnected(conn ->
                                        conn.addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                                                .addHandlerLast(new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS))
                                )
                ))
                .build()
        );
    }

    public Mono<ResponseEntity<String>> add(UserDtoAdd dto) {
        return post("", dto);
    }

    public Mono<ResponseEntity<String>> update(UserDtoUpdate dto, Long userId) {
        return patch("/" + userId, userId, dto);
    }

    public Mono<ResponseEntity<String>> remove(Long userId) {
        return delete("/" + userId, userId);
    }

    public Mono<ResponseEntity<String>> findAll() {
        return get("");
    }

    public Mono<ResponseEntity<String>> findById(Long userId) {
        return get("/" + userId, userId);
    }
}
