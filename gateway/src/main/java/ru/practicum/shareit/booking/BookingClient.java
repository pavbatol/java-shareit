package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;
import ru.practicum.shareit.client.BaseClient;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class BookingClient extends BaseClient {
    private static final String API_PREFIX = "/bookings";

    @Autowired
    public BookingClient(@Value("${shareit-server.url}") String serverUrl,
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

    public Mono<ResponseEntity<String>> add(BookItemRequestDto dto, long userId) {
        return post("", userId, dto);
    }

    public Mono<ResponseEntity<String>> approve(Long bookingId, Long userId, Boolean approved) {
        Map<String, Object> parameters = Map.of(
                "approved", approved
        );
        return patch("/" + bookingId, userId, parameters, null);
    }

    public Mono<ResponseEntity<String>> findById(Long bookingId, long userId) {
        return get("/" + bookingId, userId);
    }

    public Mono<ResponseEntity<String>> findAllByBookerId(Long bookerId, BookingState bookingState, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", bookingState.name(),
                "from", from,
                "size", size
        );
        return get("", bookerId, parameters);
    }

    public Mono<ResponseEntity<String>> findAllByOwnerId(Long ownerId, BookingState bookingState, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "state", bookingState.name(),
                "from", from,
                "size", size
        );
        return get("/owner", ownerId, parameters);
    }
}
