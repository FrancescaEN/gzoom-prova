package it.mapsgroup.gzoom.entity.orgchart.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Person {
    private String id;
    private String firstName;
    private String lastName;
    private String description;
}
