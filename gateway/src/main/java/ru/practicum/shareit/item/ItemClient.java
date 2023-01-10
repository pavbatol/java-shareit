package ru.practicum.shareit.item;

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
import ru.practicum.shareit.item.dto.CommentDtoAdd;
import ru.practicum.shareit.item.dto.ItemDtoAdd;
import ru.practicum.shareit.item.dto.ItemDtoUpdate;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl,
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

    public Mono<ResponseEntity<String>> add(ItemDtoAdd dtp, Long userId) {
        return post("", userId, dtp);
    }

    public Mono<ResponseEntity<String>> update(ItemDtoUpdate dto, Long itemId, Long userId) {
        return patch("/" + itemId, userId, dto);
    }

    public Mono<ResponseEntity<String>> findById(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public Mono<ResponseEntity<String>> findAllByUserId(Long userId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size
        );
        return get("", userId, parameters);
    }

    public Mono<ResponseEntity<String>> searchByNameOrDescription(String text, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size
        );
        return get("/search", null, parameters);
    }

    public Mono<ResponseEntity<String>> addComment(CommentDtoAdd dto, Long itemId, Long userId) {
        return post("/" + itemId + "/comment", userId, dto);
    }
}
