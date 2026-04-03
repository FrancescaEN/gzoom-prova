package it.mapsgroup.gzoom.entity.goalfile.model;

import it.mapsgroup.gzoom.entity.queryconfig.model.ResultMessage;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Results<T> {
    private T data;
    private List<ResultMessage> messages = new ArrayList<>();
}
