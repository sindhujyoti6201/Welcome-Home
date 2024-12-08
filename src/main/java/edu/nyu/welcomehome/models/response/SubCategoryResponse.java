package edu.nyu.welcomehome.models.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableSubCategoryResponse.class)
@JsonDeserialize(as = ImmutableSubCategoryResponse.class)
public interface SubCategoryResponse {
    List<String> categories();
}
