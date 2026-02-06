package local.nca.callcenter.core.api.response;

import java.time.Instant;

/**
 * Паттерн конверт - Унифицированный ответ API.
 * @param success
 * @param response
 * @param timestamp
 * @param <T>
 */
public record ApiResponse<T extends Response>(
    boolean success,
    T response,
    Instant timestamp
) {

    public static <T extends Response> ApiResponse<T> success(T response) {
        return new ApiResponse<>(true, response, Instant.now());
    }

    public static ApiResponse<ErrorResponse> error(ErrorResponse response) {
        return new ApiResponse<>(false, response, Instant.now());
    }

}
