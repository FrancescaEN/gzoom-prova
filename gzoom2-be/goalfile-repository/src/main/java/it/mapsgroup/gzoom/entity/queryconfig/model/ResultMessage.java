package it.mapsgroup.gzoom.entity.queryconfig.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ResultMessage {
    private String message;
    private String messageLang;
    private MessageLevel messageLevel;

    public enum MessageLevel {
        ERROR,
        WARNING,
        CODE_ERR,
        CODE_WARN
    }
}
