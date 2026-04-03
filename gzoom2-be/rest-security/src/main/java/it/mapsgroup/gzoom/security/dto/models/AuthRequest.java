package it.mapsgroup.gzoom.security.dto.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class AuthRequest {

    public final  String uid;
    public final  String requestor;

    @JsonCreator
    public AuthRequest(@JsonProperty("uid") String uid, @JsonProperty("requestor") String requestor) {
        super();
        this.uid = uid;
        this.requestor = requestor;
    }
}
