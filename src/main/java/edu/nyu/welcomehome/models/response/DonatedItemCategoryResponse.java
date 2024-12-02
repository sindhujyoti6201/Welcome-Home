package edu.nyu.welcomehome.models.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.annotation.Nullable;
import org.immutables.value.Value;

@Value.Immutable
@JsonSerialize(as = ImmutableDonatedItemCategoryResponse.class)
@JsonDeserialize(as = ImmutableDonatedItemCategoryResponse.class)
public interface DonatedItemCategoryResponse {
    String mainCategory();

    @Nullable
    String subCategory();

    int itemCount();
}