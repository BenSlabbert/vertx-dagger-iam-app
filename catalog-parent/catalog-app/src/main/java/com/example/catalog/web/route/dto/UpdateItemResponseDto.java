package com.example.catalog.web.route.dto;

import com.example.commons.web.serialization.JsonWriter;
import io.vertx.core.json.JsonObject;

public record UpdateItemResponseDto() implements JsonWriter {

  public UpdateItemResponseDto(JsonObject jsonObject) {
    this();
  }

  @Override
  public JsonObject toJson() {
    return new JsonObject();
  }
}
