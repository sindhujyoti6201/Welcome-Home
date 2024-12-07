package edu.nyu.welcomehome.models.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import edu.nyu.welcomehome.models.RoleType;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableRegisterRequest.class)
@JsonDeserialize(as = ImmutableRegisterRequest.class)
public interface RegisterRequest {
    String username();

    String password();

    String firstName();

    String lastName();

    String email();

    List<RoleType> roleEnrolled();
}
