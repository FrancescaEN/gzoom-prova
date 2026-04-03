package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CustomMethodDto {
    private String customMethodId;
    private String customMethodTypeId;
    private String customMethodName;
    private String description;
 }