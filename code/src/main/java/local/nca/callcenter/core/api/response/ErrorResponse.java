package local.nca.callcenter.core.api.response;

import lombok.Builder;

/**
 * Стандартизированное описание ошибки для клиента API.
 */
@Builder
public record ErrorResponse(
        String code,
        String message
)  implements Response {
}
