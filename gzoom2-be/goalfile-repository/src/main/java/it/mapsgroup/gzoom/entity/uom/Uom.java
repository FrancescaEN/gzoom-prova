package it.mapsgroup.gzoom.entity.uom;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Uom {

    private String id;
    private UomType type;
    private String abbreviation;
    private String abbreviationLang;
    private String description;
    private String descriptionLang;
    private Integer decimalScale;
    private Double minValue;
    private Double maxValue;
}