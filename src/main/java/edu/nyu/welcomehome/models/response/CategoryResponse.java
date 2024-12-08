package edu.nyu.welcomehome.models.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.immutables.value.Value;

import java.util.List;

@Value.Immutable
@JsonSerialize(as = ImmutableCategoryResponse.class)
@JsonDeserialize(as = ImmutableCategoryResponse.class)
public interface CategoryResponse {
    List<String> categories();
}
