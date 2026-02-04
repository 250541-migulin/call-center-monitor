package local.nca.callcenter.admin.web.dto;

/**
 * DTO для детальной информации о подключении.
 */
public record ConnectionDetails(
        boolean connected,
        String connectionState,
        String host,
        int port
) {}