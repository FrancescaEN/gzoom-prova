package it.mapsgroup.gzoom.infrastructure.content.dto;

import lombok.*;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileExtensionDto {
    private String fileExtensionId;
    private String mimeTypeId;
}
