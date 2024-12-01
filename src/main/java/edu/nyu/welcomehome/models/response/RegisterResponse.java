package edu.nyu.welcomehome.models.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableRegisterResponse.class)
@JsonDeserialize(as = ImmutableRegisterResponse.class)
public interface RegisterResponse {
    String status();

    String message();
}
