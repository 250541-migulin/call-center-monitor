package local.nca.callcenter.domain.model;

import lombok.*;

import java.time.LocalDateTime;

/**
 * Оператор колл-центра.
 * Часть доменной модели предметной области.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Operator {

    private String id;
    private String extension;
    private String name;
    private String status;
    private LocalDateTime lastActivity;
}