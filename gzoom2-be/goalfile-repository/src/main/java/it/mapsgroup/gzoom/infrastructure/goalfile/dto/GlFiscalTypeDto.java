package it.mapsgroup.gzoom.infrastructure.goalfile.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GlFiscalTypeDto {

        private String glFiscalTypeId;
        private String glFiscalTypeEnumId;
        private String description;
        private String descriptionLang;
}
