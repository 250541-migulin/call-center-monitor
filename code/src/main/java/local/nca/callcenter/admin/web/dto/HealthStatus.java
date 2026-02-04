package local.nca.callcenter.admin.web.dto;

/**
 * DTO для общего состояния системы.
 */
public record HealthStatus(
        String status,
        String message,
        boolean asteriskConnected
) {}