package it.mapsgroup.gzoom.entity.uom;

import lombok.*;

import java.time.OffsetDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Period {
    private PeriodType periodType;
    private String id;
    private String code;
    private String codeLang;
    private String name;
    private String nameLang;
    private OffsetDateTime fromDate;
    private OffsetDateTime thruDate;
}
